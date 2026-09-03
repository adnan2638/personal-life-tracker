package com.adnan.personallifetracker.feature.tasks.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adnan.personallifetracker.feature.tasks.domain.model.CreateTaskCommand
import com.adnan.personallifetracker.feature.tasks.domain.model.Task
import com.adnan.personallifetracker.feature.tasks.domain.model.TaskCategory
import com.adnan.personallifetracker.feature.tasks.domain.model.UpdateTaskCommand
import com.adnan.personallifetracker.feature.tasks.domain.repository.TaskCategoryRepository
import com.adnan.personallifetracker.feature.tasks.domain.repository.TaskRepository
import com.adnan.personallifetracker.feature.tasks.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TasksUiState(val tasks: List<Task> = emptyList(), val categories: List<TaskCategory> = emptyList(), val isLoading: Boolean = true, val error: String? = null)

@HiltViewModel
class TasksViewModel @Inject constructor(taskRepository: TaskRepository, categoryRepository: TaskCategoryRepository) : ViewModel() {
    private val createTask = CreateTaskUseCase(taskRepository); private val updateTask = UpdateTaskUseCase(taskRepository)
    private val completeTask = CompleteTaskUseCase(taskRepository); private val partialTask = MarkTaskPartialUseCase(taskRepository)
    private val startTask = StartTaskUseCase(taskRepository); private val archiveTask = ArchiveTaskUseCase(taskRepository)
    private val createTaskCategory = CreateTaskCategoryUseCase(categoryRepository)
    private val _error = MutableStateFlow<String?>(null)
    val state: StateFlow<TasksUiState> = combine(
        ObserveTasksUseCase(taskRepository)(),
        ObserveTaskCategoriesUseCase(categoryRepository)(),
        _error,
    ) { tasks: List<Task>, categories: List<TaskCategory>, error: String? ->
        TasksUiState(tasks, categories, false, error)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TasksUiState())
    fun save(command: CreateTaskCommand, onDone: () -> Unit) = run { viewModelScope.launch { runCatching { createTask(command) }.onSuccess { onDone() }.onFailure { _error.value = it.message } } }
    fun update(command: UpdateTaskCommand, onDone: () -> Unit) = run { viewModelScope.launch { runCatching { updateTask(command) }.onSuccess { onDone() }.onFailure { _error.value = it.message } } }
    fun createCategory(name: String) = viewModelScope.launch { runCatching { createTaskCategory(name) }.onFailure { _error.value = it.message } }
    fun start(id: String) = action { startTask(id) }; fun complete(id: String) = action { completeTask(id) }; fun partial(id: String) = action { partialTask(id) }; fun archive(id: String, onDone: () -> Unit) = viewModelScope.launch { runCatching { archiveTask(id) }.onSuccess { onDone() }.onFailure { _error.value = it.message } }
    private fun action(block: suspend () -> Unit) = viewModelScope.launch { runCatching { block() }.onFailure { _error.value = it.message } }
}
