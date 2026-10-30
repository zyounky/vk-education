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
        lateinit var originalState: AppListUiState
        composeRule.activityRule.scenario.onActivity { activity ->
            originalViewModel = ViewModelProvider(activity)[AppListViewModel::class.java]
            originalState = originalViewModel.uiState.value
        }

        composeRule.onNodeWithContentDescription("Показать информацию: Telegram").performClick()
        composeRule.onNodeWithText("Вы выбрали Telegram").assertIsDisplayed()

        composeRule.activityRule.scenario.recreate()
        composeRule.waitForIdle()

        composeRule.activityRule.scenario.onActivity { activity ->
            val retainedViewModel = ViewModelProvider(activity)[AppListViewModel::class.java]
            assertSame(originalViewModel, retainedViewModel)
            assertSame(originalState, retainedViewModel.uiState.value)
        }
        composeRule.onNodeWithText("Telegram").assertIsDisplayed()
        composeRule.onNodeWithText("Вы выбрали Telegram").assertDoesNotExist()

        composeRule.onNodeWithContentDescription("Показать информацию: Telegram").performClick()
        composeRule.onNodeWithText("Вы выбрали Telegram").assertIsDisplayed()
    }
}
