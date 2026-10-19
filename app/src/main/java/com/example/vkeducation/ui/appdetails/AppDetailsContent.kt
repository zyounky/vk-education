package com.example.vkeducation.ui.appdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vkeducation.model.AppDetails
import com.example.vkeducation.ui.components.AppShowcase

// Adapted from AppDetailsContent in maltsevmicyou/VkEducation, b4c673b.
// The practical card is retained; the homework adds AppShowcase at its bottom.
@Composable
fun AppDetailsContent(
    appDetails: AppDetails,
    descriptionExpanded: Boolean,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onInstallClick: () -> Unit,
    onReadMoreClick: () -> Unit,
    onDeveloperClick: () -> Unit,
    onShowcaseAppClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.verticalScroll(rememberScrollState())) {
        Toolbar(onBackClick = onBackClick, onShareClick = onShareClick)
        Spacer(Modifier.height(8.dp))
        AppDetailsHeader(
            appDetails = appDetails,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(Modifier.height(16.dp))
        InstallButton(
            onClick = onInstallClick,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        )
        Spacer(Modifier.height(12.dp))
        ScreenshotsList(
            screenshotResList = appDetails.screenshotResList,
            contentPadding = PaddingValues(horizontal = 16.dp),
        )
        Spacer(Modifier.height(12.dp))
        AppDescription(
            description = appDetails.description,
            expanded = descriptionExpanded,
            onReadMoreClick = onReadMoreClick,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        )
        Spacer(Modifier.height(12.dp))
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        Spacer(Modifier.height(12.dp))
        Developer(
            name = appDetails.developer,
            onClick = onDeveloperClick,
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
        )
        Spacer(Modifier.height(24.dp))
        AppShowcase(onAppClick = onShowcaseAppClick)
        Spacer(Modifier.height(24.dp))
    }
}
