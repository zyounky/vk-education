package com.example.vkeducation.presentation

import com.example.vkeducation.data.RatingsRepository
import com.example.vkeducation.data.SimulatedRatingsApi
import com.example.vkeducation.domain.model.AppInfo
import com.example.vkeducation.domain.repository.AppsRepository
import java.io.IOException
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppListViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val sampleApp = AppInfo("telegram", "Telegram", "Messenger", "Communication")

    private fun createViewModel(repository: AppsRepository) =
        AppListViewModel(repository, RatingsRepository(SimulatedRatingsApi()))

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
        val viewModel = createViewModel {
            loads++
            listOf(sampleApp)
        }

        testScheduler.runCurrent()
        val firstScreenState = viewModel.uiState.first()
        val recreatedScreenState = viewModel.uiState.first()

        assertEquals(AppListUiState.Loaded(listOf(sampleApp)), firstScreenState)
        assertSame(firstScreenState, recreatedScreenState)
        assertEquals(1, loads)
    }

    @Test
    fun failedLoadCanBeRetriedWithoutStartingDuplicateRequests() = runTest(dispatcher) {
        var loads = 0
        val viewModel = createViewModel {
            loads++
            if (loads == 1) throw IOException("Offline")
            listOf(sampleApp)
        }

        assertEquals(AppListUiState.Loading, viewModel.uiState.value)
        testScheduler.runCurrent()
        assertEquals(AppListUiState.Error, viewModel.uiState.value)

        viewModel.retry()
        viewModel.retry()
        assertEquals(AppListUiState.Loading, viewModel.uiState.value)
        testScheduler.runCurrent()

        assertEquals(AppListUiState.Loaded(listOf(sampleApp)), viewModel.uiState.value)
        assertEquals(2, loads)
    }

    @Test
    fun comparesSequentialAndConcurrentRatingLoadsOverTheSameIds() = runTest(dispatcher) {
        val apps = listOf(sampleApp, sampleApp.copy(id = "second"))
        val viewModel = createViewModel { apps }
        testScheduler.runCurrent()

        viewModel.compareRatings()
        viewModel.compareRatings()
        assertEquals(RatingsStage.Sequential, viewModel.currentRatings().stage)
        testScheduler.advanceTimeBy(2_999)
        testScheduler.runCurrent()
        assertEquals(RatingsStage.Sequential, viewModel.currentRatings().stage)
        assertEquals(emptyMap<String, Float>(), viewModel.currentRatings().byAppId)

        testScheduler.advanceTimeBy(1)
        testScheduler.runCurrent()
        val sequential = viewModel.currentRatings()
        assertEquals(RatingsStage.Parallel, sequential.stage)
        assertTrue(sequential.sequentialMillis != null)
        assertNull(sequential.parallelMillis)
        assertEquals(apps.map { it.id }.toSet(), sequential.byAppId.keys)
        assertTrue(sequential.byAppId.values.all { it in 3f..5f })

        testScheduler.advanceTimeBy(1_500)
        testScheduler.runCurrent()
        val complete = viewModel.currentRatings()
        assertEquals(RatingsStage.Idle, complete.stage)
        assertEquals(sequential.sequentialMillis, complete.sequentialMillis)
        assertTrue(complete.parallelMillis != null)
        assertEquals(sequential.byAppId, complete.byAppId)
    }

    @Test
    fun consumedSnackbarIsNotReplayedToRecreatedCollector() = runTest(dispatcher) {
        val viewModel = createViewModel { listOf(sampleApp) }
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
        val viewModel = createViewModel { listOf(sampleApp) }
        viewModel.onAppLogoClicked(sampleApp)
        testScheduler.runCurrent()

        assertEquals(AppListEvent.ShowAppInfo("Telegram"), viewModel.events.first())
        assertNull(withTimeoutOrNull(100) { viewModel.events.first() })
    }

    private fun AppListViewModel.currentRatings() =
        (uiState.value as AppListUiState.Loaded).ratings
}
