package com.example.vkeducation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import org.junit.Rule
import org.junit.Test

class ShowcaseNavigationTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun eachShowcasePageReturnsToTheListAndPagerStopsAfterThreePages() {
        val pageApplications = listOf("Telegram", "Музыка", "Книги")
        pageApplications.forEachIndexed { page, appName ->
            compose.onNodeWithText("Telegram").performClick()
            compose.onNodeWithTag("showcase_page_number").performScrollTo()
            repeat(page) {
                compose.onNodeWithTag("showcase_pager").performTouchInput { swipeLeft(startX = width * 0.8f, endX = width * 0.2f, durationMillis = 500) }
                compose.waitForIdle()
                compose.onNodeWithTag("showcase_page_number").performScrollTo()
            }
            compose.onNodeWithTag("showcase_page_number").assertTextEquals("${page + 1} из 3").assertIsDisplayed()
            if (page == 2) {
                compose.onNodeWithTag("showcase_pager").performTouchInput { swipeLeft(startX = width * 0.8f, endX = width * 0.2f, durationMillis = 500) }
                compose.waitForIdle()
                compose.onNodeWithText("3 из 3").assertIsDisplayed()
            }
            compose.onNode(
                hasText(appName) and hasAnyAncestor(hasTestTag("showcase_pager")),
            ).performClick()
            compose.onNodeWithTag("apps_list").assertIsDisplayed()
            compose.onNodeWithText("Telegram").assertIsDisplayed()
        }
    }

    @Test
    fun toolbarBackReturnsFromTheReadyMadeCard() {
        compose.onNodeWithText("Telegram").performClick()
        compose.onNodeWithText("Гильдия Героев: Экшен ММО РПГ").assertIsDisplayed()
        compose.onNodeWithContentDescription("К списку приложений").performClick()
        compose.onNodeWithTag("apps_list").assertIsDisplayed()
    }
}
