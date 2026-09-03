package com.adnan.personallifetracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adnan.personallifetracker.feature.dashboard.presentation.DashboardRoute
import com.adnan.personallifetracker.feature.tasks.presentation.TaskDetailsRoute
import com.adnan.personallifetracker.feature.tasks.presentation.TaskFormRoute
import com.adnan.personallifetracker.feature.tasks.presentation.TasksRoute

@Composable
fun PersonalLifeTrackerNavHost() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = AppDestination.Dashboard.route) {
        composable(AppDestination.Dashboard.route) { DashboardRoute(onTasks = { navController.navigate(AppDestination.Tasks.route) }) }
        composable(AppDestination.Tasks.route) { TasksRoute(onAdd = { navController.navigate(AppDestination.AddTask.route) }, onTask = { navController.navigate("tasks/$it") }) }
        composable(AppDestination.AddTask.route) { TaskFormRoute(null, onDone = { navController.popBackStack() }, onBack = { navController.popBackStack() }) }
        composable(AppDestination.TaskDetails.route) { entry -> val id = requireNotNull(entry.arguments?.getString("taskId")); TaskDetailsRoute(id, onEdit = { navController.navigate("tasks/$it/edit") }, onBack = { navController.popBackStack() }) }
        composable(AppDestination.EditTask.route) { entry -> val id = requireNotNull(entry.arguments?.getString("taskId")); TaskFormRoute(id, onDone = { navController.popBackStack() }, onBack = { navController.popBackStack() }) }
    }
}
