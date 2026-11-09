package com.example.vkeducation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.vkeducation.presentation.AppListRoute
import com.example.vkeducation.presentation.AppListViewModel
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
                AppListRoute(viewModel = viewModel)
            }
        }
    }
}
