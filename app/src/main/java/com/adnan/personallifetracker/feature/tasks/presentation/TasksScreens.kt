package com.adnan.personallifetracker.feature.tasks.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.adnan.personallifetracker.feature.tasks.domain.model.*
import java.time.*
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun TasksRoute(onAdd: () -> Unit, onTask: (String) -> Unit, viewModel: TasksViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val now = System.currentTimeMillis()
    val tasks = state.tasks.sortedWith(compareBy<Task> { !it.isUnresolvedOverdue(now) }.thenBy { it.priority != TaskPriority.HIGH }.thenBy { it.startAt ?: Long.MAX_VALUE }.thenBy { it.deadlineAt ?: Long.MAX_VALUE })
    Scaffold(topBar = { TopAppBar(title = { Text("Tasks") }) }, floatingActionButton = { FloatingActionButton(onClick = onAdd) { Text("+") } }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item { Text("Today", style = MaterialTheme.typography.titleLarge) }
            when { state.isLoading -> item { CircularProgressIndicator() }; tasks.isEmpty() -> item { Text("No tasks yet. Add a task to get started.") }; else -> items(tasks, key = Task::id) { task -> TaskCard(task, state.categories.firstOrNull { it.id == task.categoryId }?.name) { onTask(task.id) } } }
        }
    }
}

@Composable private fun TaskCard(task: Task, category: String?, onClick: () -> Unit) = Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(task.title, style = MaterialTheme.typography.titleMedium); Text(task.priority.name) }
    category?.let { Text(it, style = MaterialTheme.typography.bodySmall) }; Text(task.status.name.replace('_', ' '), style = MaterialTheme.typography.bodySmall)
    task.startAt?.let { Text("Start: ${formatTime(it)}") }; task.deadlineAt?.let { Text("Deadline: ${formatTime(it)}") }; if (task.completionTiming == CompletionTiming.LATE) Text("Late", color = MaterialTheme.colorScheme.error)
} }

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TaskFormRoute(taskId: String?, onDone: () -> Unit, onBack: () -> Unit, viewModel: TasksViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState(); val existing = state.tasks.firstOrNull { it.id == taskId }
    var title by remember(existing) { mutableStateOf(existing?.title.orEmpty()) }; var notes by remember(existing) { mutableStateOf(existing?.notes.orEmpty()) }; var date by remember(existing) { mutableStateOf(existing?.date ?: LocalDate.now().toString()) }
    var start by remember(existing) { mutableStateOf(existing?.startAt?.let(::formatInputTime).orEmpty()) }; var deadline by remember(existing) { mutableStateOf(existing?.deadlineAt?.let(::formatInputTime).orEmpty()) }; var duration by remember(existing) { mutableStateOf(existing?.estimatedDurationMinutes?.toString().orEmpty()) }; var categoryId by remember(existing) { mutableStateOf(existing?.categoryId) }; var priority by remember(existing) { mutableStateOf(existing?.priority ?: TaskPriority.MEDIUM) }
    Scaffold(topBar = { TopAppBar(title = { Text(if (taskId == null) "Add Task" else "Edit Task") }, navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }) }) { padding -> LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { OutlinedTextField(title, { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth(), isError = state.error?.contains("title", true) == true) }
        item { OutlinedTextField(notes, { notes = it }, label = { Text("Notes") }, modifier = Modifier.fillMaxWidth(), minLines = 2) }
        item { OutlinedTextField(date, { date = it }, label = { Text("Date (YYYY-MM-DD)") }, supportingText = { Text("Example: 2026-09-02") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(start, { start = it }, label = { Text("Start time (optional)") }, supportingText = { Text("24-hour time, e.g. 09:30") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(deadline, { deadline = it }, label = { Text("Deadline (optional)") }, supportingText = { Text("24-hour time, e.g. 17:00") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(duration, { duration = it }, label = { Text("Estimated minutes (optional)") }, modifier = Modifier.fillMaxWidth()) }
        item { Text("Priority"); Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { TaskPriority.entries.forEach { item -> FilterChip(selected = priority == item, onClick = { priority = item }, label = { Text(item.name) }) } } }
        item { CategoryPicker(state.categories, categoryId, { categoryId = it }, viewModel::createCategory) }
        state.error?.let { error -> item { Text(error, color = MaterialTheme.colorScheme.error) } }
        item { Button(onClick = { val startAt = parseLocalTime(date, start); val deadlineAt = parseLocalTime(date, deadline); val estimated = duration.takeIf(String::isNotBlank)?.toIntOrNull(); if (taskId == null) viewModel.save(CreateTaskCommand(title, notes.ifBlank { null }, categoryId, date, startAt, deadlineAt, priority, estimated), onDone) else viewModel.update(UpdateTaskCommand(taskId, title, notes.ifBlank { null }, categoryId, date, startAt, deadlineAt, priority, estimated), onDone) }, modifier = Modifier.fillMaxWidth()) { Text("Save task") } }
    } }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CategoryPicker(categories: List<TaskCategory>, selected: String?, onSelected: (String?) -> Unit, onCreate: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }; var name by remember { mutableStateOf("") }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Category"); OutlinedButton(onClick = { expanded = true }) { Text(categories.firstOrNull { it.id == selected }?.name ?: "None") }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) { DropdownMenuItem(text = { Text("None") }, onClick = { onSelected(null); expanded = false }); categories.forEach { category -> DropdownMenuItem(text = { Text(category.name) }, onClick = { onSelected(category.id); expanded = false }) } }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { OutlinedTextField(name, { name = it }, label = { Text("New category") }, modifier = Modifier.weight(1f)); TextButton(onClick = { if (name.isNotBlank()) { onCreate(name); name = "" } }) { Text("Add") } }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TaskDetailsRoute(id: String, onEdit: (String) -> Unit, onBack: () -> Unit, viewModel: TasksViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState(); val task = state.tasks.firstOrNull { it.id == id }
    Scaffold(topBar = { TopAppBar(title = { Text("Task details") }, navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }) }) { padding -> if (task == null) Box(Modifier.fillMaxSize().padding(padding).padding(16.dp)) { Text("Task not found") } else Column(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(task.title, style = MaterialTheme.typography.headlineSmall); task.notes?.let { Text(it) }; Text("Date: ${task.date}"); Text("Priority: ${task.priority}"); Text("Status: ${task.status}")
        task.startAt?.let { Text("Start: ${formatTime(it)}") }; task.deadlineAt?.let { Text("Deadline: ${formatTime(it)}") }; task.estimatedDurationMinutes?.let { Text("Estimated: $it min") }; task.completionTiming?.let { Text("Completion: $it") }; task.completedAt?.let { Text("Completed: ${formatTime(it)}") }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { if (task.status == TaskStatus.PLANNED) Button({ viewModel.start(id) }) { Text("Start") }; if (task.status != TaskStatus.COMPLETED) Button({ viewModel.complete(id) }) { Text("Complete") }; if (task.status == TaskStatus.PLANNED || task.status == TaskStatus.IN_PROGRESS) OutlinedButton({ viewModel.partial(id) }) { Text("Partial") } }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { OutlinedButton({ onEdit(id) }) { Text("Edit") }; TextButton({ viewModel.archive(id, onBack) }) { Text("Archive") } }
    } }
}

private fun Task.isUnresolvedOverdue(now: Long) = deadlineAt != null && deadlineAt < now && status != TaskStatus.COMPLETED && status != TaskStatus.MISSED
private fun parseLocalTime(date: String, value: String): Long? = if (value.isBlank()) null else runCatching { LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(value)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() }.getOrNull()
private fun formatTime(value: Long): String = Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("MMM d, HH:mm"))
private fun formatInputTime(value: Long): String = Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
