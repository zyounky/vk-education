package com.example.vkeducation.presentation

import com.example.vkeducation.domain.repository.AppsRepository
import com.example.vkeducation.domain.model.AppIcon
import com.example.vkeducation.domain.model.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppListViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val sampleApp = AppInfo(AppIcon.TELEGRAM, "Telegram", "Messenger", "Communication")

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun restartingStateCollectorsDoesNotLoadContentAgain() = runTest(dispatcher) {
        var loads = 0
        val viewModel = AppListViewModel(AppsRepository {
            loads++
            listOf(sampleApp)
        })

        val firstScreenState = viewModel.uiState.first()
        val recreatedScreenState = viewModel.uiState.first()

        assertEquals(listOf(sampleApp), firstScreenState.applications)
        assertSame(firstScreenState, recreatedScreenState)
        assertEquals(1, loads)
    }

    @Test
    fun consumedSnackbarIsNotReplayedToRecreatedCollector() = runTest(dispatcher) {
        val viewModel = AppListViewModel(AppsRepository { listOf(sampleApp) })
        viewModel.onAppLogoClicked(sampleApp)

        assertEquals(AppListEvent.ShowAppInfo("Telegram"), viewModel.events.first())
        assertNull(withTimeoutOrNull(100) { viewModel.events.first() })

        // A fresh click still works after the screen's collector was replaced.
        viewModel.onAppLogoClicked(sampleApp)
        assertEquals(AppListEvent.ShowAppInfo("Telegram"), viewModel.events.first())
        assertNull(withTimeoutOrNull(100) { viewModel.events.first() })
    }

    @Test
    fun clickAwaitingAnActiveScreenIsDeliveredOnlyOnce() = runTest(dispatcher) {
        val viewModel = AppListViewModel(AppsRepository { listOf(sampleApp) })
        viewModel.onAppLogoClicked(sampleApp)
        testScheduler.runCurrent()

        assertEquals(AppListEvent.ShowAppInfo("Telegram"), viewModel.events.first())
        assertNull(withTimeoutOrNull(100) { viewModel.events.first() })
    }
}
