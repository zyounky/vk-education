package com.example.vkeducation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.ViewModelProvider
import com.example.vkeducation.presentation.AppListUiState
import com.example.vkeducation.presentation.AppListViewModel
import org.junit.Assert.assertSame
import org.junit.Rule
import org.junit.Test

class AppListLifecycleTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun recreationRetainsContentButDoesNotReplaySnackbar() {
        lateinit var originalViewModel: AppListViewModel
        composeRule.activityRule.scenario.onActivity { activity ->
            originalViewModel = ViewModelProvider(activity)[AppListViewModel::class.java]
        }

        composeRule.waitUntil(timeoutMillis = 20_000) {
            originalViewModel.uiState.value is AppListUiState.Loaded
        }
        val originalState = originalViewModel.uiState.value as AppListUiState.Loaded
        val firstAppName = originalState.applications.first().name
        val logoLabel = composeRule.activity.getString(R.string.show_app_info, firstAppName)
        val selectedMessage = composeRule.activity.getString(R.string.app_selected, firstAppName)

        composeRule.onNodeWithContentDescription(logoLabel).performClick()
        composeRule.onNodeWithText(selectedMessage).assertIsDisplayed()

        composeRule.activityRule.scenario.recreate()
        composeRule.waitForIdle()

        composeRule.activityRule.scenario.onActivity { activity ->
            val retainedViewModel = ViewModelProvider(activity)[AppListViewModel::class.java]
            assertSame(originalViewModel, retainedViewModel)
            assertSame(originalState, retainedViewModel.uiState.value)
        }
        composeRule.onNodeWithText(firstAppName).assertIsDisplayed()
        composeRule.onNodeWithText(selectedMessage).assertDoesNotExist()

        composeRule.onNodeWithContentDescription(logoLabel).performClick()
        composeRule.onNodeWithText(selectedMessage).assertIsDisplayed()
    }
}
