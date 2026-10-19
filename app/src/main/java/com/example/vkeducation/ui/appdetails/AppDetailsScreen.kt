package com.example.vkeducation.ui.appdetails

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import com.example.vkeducation.R
import com.example.vkeducation.data.practiceAppDetails
import kotlinx.coroutines.launch

@Composable
fun AppDetailsScreen(onBackClick: () -> Unit, onShowcaseAppClick: () -> Unit) {
    var descriptionExpanded by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val view = LocalView.current
    val darkTheme = isSystemInDarkTheme()
    val sampleMessage = stringResource(R.string.sample_card_action)
    val shareTitle = stringResource(R.string.share_app)
    val showSampleMessage: () -> Unit = {
        scope.launch { snackbarHostState.showSnackbar(sampleMessage) }
    }

    // The list has a blue toolbar; this card needs dark status icons in light mode.
    DisposableEffect(view, darkTheme) {
        val controller = (view.context as? Activity)?.window?.let {
            WindowCompat.getInsetsController(it, view)
        }
        val previousAppearance = controller?.isAppearanceLightStatusBars
        controller?.isAppearanceLightStatusBars = !darkTheme
        onDispose {
            if (previousAppearance != null) {
                controller.isAppearanceLightStatusBars = previousAppearance
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        AppDetailsContent(
            appDetails = practiceAppDetails,
            descriptionExpanded = descriptionExpanded,
            onBackClick = onBackClick,
            onShareClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, practiceAppDetails.name + "\n" + practiceAppDetails.description)
                }
                context.startActivity(Intent.createChooser(intent, shareTitle))
            },
            onInstallClick = showSampleMessage,
            onReadMoreClick = { descriptionExpanded = true },
            onDeveloperClick = showSampleMessage,
            onShowcaseAppClick = onShowcaseAppClick,
            modifier = Modifier.fillMaxSize().padding(padding),
        )
    }
}
