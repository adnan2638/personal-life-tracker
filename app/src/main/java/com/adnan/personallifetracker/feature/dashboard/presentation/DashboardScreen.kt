package com.adnan.personallifetracker.feature.dashboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.adnan.personallifetracker.feature.tasks.domain.model.TaskStatus
import com.adnan.personallifetracker.feature.tasks.presentation.TasksViewModel
import java.time.LocalDate

@Composable fun DashboardRoute(onTasks: () -> Unit, viewModel: TasksViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    DashboardScreen(onTasks, state.tasks.filter { it.date == LocalDate.now().toString() && it.status != TaskStatus.COMPLETED })
}

@Composable
private fun DashboardScreen(onTasks: () -> Unit, todayTasks: List<com.adnan.personallifetracker.feature.tasks.domain.model.Task>) {
    Column(Modifier.fillMaxSize().padding(24.dp), Arrangement.Center, Alignment.CenterHorizontally) {
        Text("Dashboard", style = MaterialTheme.typography.headlineMedium)
        Text("Your personal tracker foundation is ready.")
        Spacer(Modifier.padding(8.dp))
        Text("Today's Tasks", style = MaterialTheme.typography.titleMedium)
        Text(if (todayTasks.isEmpty()) "No unresolved tasks today." else "${todayTasks.size} unresolved task(s) today.")
        Button(onClick = onTasks) { Text("Open Tasks") }
    }
}
