package com.adnan.personallifetracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adnan.personallifetracker.feature.dashboard.presentation.DashboardRoute

@Composable
fun PersonalLifeTrackerNavHost() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = AppDestination.Dashboard.route) { composable(AppDestination.Dashboard.route) { DashboardRoute() } }
}
