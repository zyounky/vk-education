package com.example.vkeducation.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vkeducation.R
import com.example.vkeducation.domain.model.AppInfo
import com.example.vkeducation.presentation.AppListUiState
import com.example.vkeducation.presentation.RatingsStage
import com.example.vkeducation.presentation.RatingsUiState
import com.example.vkeducation.presentation.ui.components.AppItem
import com.example.vkeducation.presentation.ui.theme.VKEDUCATIONTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListScreen(
    modifier: Modifier = Modifier,
    state: AppListUiState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onAppClick: (AppInfo) -> Unit = {},
    onAppLogoClick: (AppInfo) -> Unit = {},
    onCompareRatings: () -> Unit = {},
    onRetry: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.catalog_title)) },
                navigationIcon = {
                    Image(
                        painter = painterResource(R.drawable.ic_catalog_app),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier.padding(start = 16.dp, end = 12.dp).size(36.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1967D2),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                AppListUiState.Loading -> CircularProgressIndicator()
                AppListUiState.Error -> {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.catalog_load_error),
                            textAlign = TextAlign.Center
                        )
                        Button(onClick = onRetry) {
                            Text(stringResource(R.string.catalog_retry))
                        }
                    }
                }
                is AppListUiState.Loaded -> AppCatalog(
                    applications = state.applications,
                    ratings = state.ratings,
                    onAppClick = onAppClick,
                    onAppLogoClick = onAppLogoClick,
                    onCompareRatings = onCompareRatings,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

@Composable
private fun AppCatalog(
    applications: List<AppInfo>,
    ratings: RatingsUiState,
    onAppClick: (AppInfo) -> Unit,
    onAppLogoClick: (AppInfo) -> Unit,
    onCompareRatings: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.widthIn(max = 720.dp).fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "heading") {
            Column(
                modifier = Modifier.padding(bottom = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.collection_title),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = pluralStringResource(
                        R.plurals.app_count, applications.size, applications.size
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (applications.isNotEmpty()) {
                    RatingsComparison(ratings = ratings, onCompare = onCompareRatings)
                }
            }
        }
        items(applications, key = { it.id }) { app ->
            AppItem(
                app = app,
                rating = ratings.byAppId[app.id],
                onClick = { onAppClick(app) },
                onLogoClick = { onAppLogoClick(app) }
            )
        }
    }
}

@Composable
private fun RatingsComparison(ratings: RatingsUiState, onCompare: () -> Unit) {
    Button(onClick = onCompare, enabled = !ratings.isComparing) {
        Text(stringResource(R.string.ratings_compare))
    }
    ratings.sequentialMillis?.let { millis ->
        Text(stringResource(R.string.ratings_sequential_time, millis))
    }
    ratings.parallelMillis?.let { millis ->
        Text(stringResource(R.string.ratings_parallel_time, millis))
    }
    when (ratings.stage) {
        RatingsStage.Idle -> Unit
        RatingsStage.Sequential -> Text(stringResource(R.string.ratings_sequential_loading))
        RatingsStage.Parallel -> Text(stringResource(R.string.ratings_parallel_loading))
        RatingsStage.Failed -> Text(
            text = stringResource(R.string.ratings_load_error),
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Preview(name = "Phone", showBackground = true, widthDp = 360, heightDp = 800)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, widthDp = 360, heightDp = 800)
@Preview(name = "Large text", fontScale = 2f, widthDp = 360, heightDp = 800)
@Preview(name = "Tablet", widthDp = 840, heightDp = 800)
@Composable
private fun AppListPreview() {
    VKEDUCATIONTheme {
        AppListScreen(
            state = AppListUiState.Loaded(
                listOf(
                    AppInfo(
                        id = "preview",
                        name = "Preview app",
                        description = "Sample description",
                        category = "Sample category"
                    )
                )
            )
        )
    }
}
