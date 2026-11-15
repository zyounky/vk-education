package com.example.vkeducation.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.vkeducation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailsScreen(viewModel: AppDetailsViewModel, onBackClick: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_details_title)) },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text(stringResource(R.string.app_details_back), color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1967D2),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val current = state) {
                AppDetailsState.Loading -> CircularProgressIndicator()
                AppDetailsState.Error -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(stringResource(R.string.app_details_error))
                    Button(onClick = viewModel::retry) {
                        Text(stringResource(R.string.catalog_retry))
                    }
                }
                is AppDetailsState.Content -> AppDetailsContent(
                    state = current,
                    onInstallClick = viewModel::onInstallClick
                )
            }
        }
    }
}

@Composable
private fun AppDetailsContent(state: AppDetailsState.Content, onInstallClick: () -> Unit) {
    val app = state.app
    val placeholder = painterResource(R.drawable.ic_catalog_app)
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = app.iconUrl,
                contentDescription = null,
                placeholder = placeholder,
                error = placeholder,
                fallback = placeholder,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(72.dp).clip(RoundedCornerShape(16.dp))
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(app.name, style = MaterialTheme.typography.headlineSmall)
                Text(app.developer, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = stringResource(
                        R.string.app_details_metadata,
                        app.category,
                        app.ageRating,
                        app.size
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        InstallButton(state = state.installState, onClick = onInstallClick)

        Text(
            stringResource(R.string.app_details_description),
            style = MaterialTheme.typography.titleMedium
        )
        Text(app.description, style = MaterialTheme.typography.bodyMedium)

        if (app.screenshots.isNotEmpty()) {
            Text(
                stringResource(R.string.app_details_screenshots),
                style = MaterialTheme.typography.titleMedium
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(app.screenshots) { index, url ->
                    AsyncImage(
                        model = url,
                        contentDescription = stringResource(R.string.app_details_screenshot, index + 1),
                        placeholder = placeholder,
                        error = placeholder,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.width(240.dp).aspectRatio(16f / 9)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
            }
        }
    }
}

@Composable
private fun InstallButton(state: InstallState, onClick: () -> Unit) {
    val label = when (state) {
        InstallState.NotInstalled -> R.string.app_details_install
        is InstallState.Installing -> R.string.app_details_cancel_install
        InstallState.Installed -> R.string.app_details_installed
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = onClick,
            enabled = state != InstallState.Installed,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(label))
        }
        if (state is InstallState.Installing) {
            LinearProgressIndicator(
                progress = { state.progress / 100f },
                modifier = Modifier.fillMaxWidth()
            )
            Text(stringResource(R.string.app_details_install_progress, state.progress))
        }
    }
}
