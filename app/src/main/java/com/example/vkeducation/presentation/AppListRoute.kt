package com.example.vkeducation.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vkeducation.R
import com.example.vkeducation.ui.AppListScreen

@Composable
fun AppListRoute(
    modifier: Modifier = Modifier,
    viewModel: AppListViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val resources = LocalResources.current

    LaunchedEffect(viewModel, lifecycleOwner, resources) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    is AppListEvent.ShowAppInfo -> snackbarHostState.showSnackbar(
                        resources.getString(R.string.app_selected, event.appName)
                    )
                }
            }
        }
    }

    AppListScreen(
        modifier = modifier,
        applications = state.applications,
        snackbarHostState = snackbarHostState,
        onAppLogoClick = viewModel::onAppLogoClicked
    )
}
