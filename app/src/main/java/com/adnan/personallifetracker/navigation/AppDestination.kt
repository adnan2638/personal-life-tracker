package com.adnan.personallifetracker.navigation

sealed interface AppDestination {
    val route: String
    data object Dashboard : AppDestination { override val route = "dashboard" }
    data object Tasks : AppDestination { override val route = "tasks" }
    data object AddTask : AppDestination { override val route = "tasks/add" }
    data object TaskDetails : AppDestination { override val route = "tasks/{taskId}" }
    data object EditTask : AppDestination { override val route = "tasks/{taskId}/edit" }
}
