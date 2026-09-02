package com.adnan.personallifetracker.navigation

sealed interface AppDestination {
    val route: String
    data object Dashboard : AppDestination { override val route = "dashboard" }
}
