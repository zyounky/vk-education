package com.example.vkeducation

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vkeducation.presentation.AppListRoute
import com.example.vkeducation.presentation.AppListViewModel
import com.example.vkeducation.presentation.details.APP_ID_ARGUMENT
import com.example.vkeducation.presentation.details.AppDetailsScreen
import com.example.vkeducation.presentation.ui.theme.VKEDUCATIONTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        val viewModel = ViewModelProvider(this)[AppListViewModel::class.java]
        setContent {
            VKEDUCATIONTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "catalog") {
                    composable("catalog") {
                        AppListRoute(
                            viewModel = viewModel,
                            onAppClick = { app ->
                                navController.navigate("details/${Uri.encode(app.id)}") {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                    composable(
                        route = "details/{$APP_ID_ARGUMENT}",
                        arguments = listOf(navArgument(APP_ID_ARGUMENT) { type = NavType.StringType })
                    ) {
                        AppDetailsScreen(
                            viewModel = hiltViewModel(),
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
