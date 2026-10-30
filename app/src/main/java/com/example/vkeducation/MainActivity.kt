package com.example.vkeducation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.vkeducation.data.LocalAppsRepository
import com.example.vkeducation.presentation.AppListRoute
import com.example.vkeducation.presentation.AppListViewModel
import com.example.vkeducation.presentation.ui.theme.VKEDUCATIONTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        // The composition root knows the concrete implementation. Presentation
        // receives only the Domain repository contract through its ViewModel.
        val viewModel = ViewModelProvider(
            this,
            AppListViewModel.factory(LocalAppsRepository())
        )[AppListViewModel::class.java]
        setContent {
            VKEDUCATIONTheme {
                AppListRoute(viewModel = viewModel)
            }
        }
    }
}
