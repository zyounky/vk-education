package com.example.vkeducation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vkeducation.ui.appdetails.AppDetailsScreen

private const val APPS_ROUTE = "apps"
private const val DETAILS_ROUTE = "app_details"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = APPS_ROUTE) {
        composable(APPS_ROUTE) {
            AppListScreen(onAppClick = {
                navController.navigate(DETAILS_ROUTE) { launchSingleTop = true }
            })
        }
        composable(DETAILS_ROUTE) {
            AppDetailsScreen(
                onBackClick = { navController.popBackStack() },
                onShowcaseAppClick = {
                    // Return to the existing list; do not add a second copy to the stack.
                    navController.popBackStack(APPS_ROUTE, inclusive = false)
                },
            )
        }
    }
}
