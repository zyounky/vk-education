package com.example.vkeducation.presentation.details

import androidx.lifecycle.SavedStateHandle
import com.example.vkeducation.data.AppDetailsDto
import com.example.vkeducation.data.AppDetailsRepository
import com.example.vkeducation.data.AppDto
import com.example.vkeducation.data.AppsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppDetailsViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun cancellingMidInstallStopsProgressAndAllowsRestart() = runTest(dispatcher) {
        val viewModel = createViewModel()
        runCurrent()

        viewModel.onInstallClick()
        assertEquals(InstallState.Installing(0), viewModel.installState)
        advanceTimeBy(450)
        runCurrent()
        assertEquals(InstallState.Installing(15), viewModel.installState)

        viewModel.onInstallClick()
        assertEquals(InstallState.NotInstalled, viewModel.installState)
        advanceTimeBy(3_000)
        runCurrent()
        assertEquals(InstallState.NotInstalled, viewModel.installState)

        viewModel.onInstallClick()
        advanceTimeBy(3_000)
        runCurrent()
        assertEquals(InstallState.Installed, viewModel.installState)
        viewModel.onInstallClick()
        assertEquals(InstallState.Installed, viewModel.installState)
    }

    @Test
    fun immediateCancelAndRestartUsesTheNewInstallJob() = runTest(dispatcher) {
        val viewModel = createViewModel()
        viewModel.onInstallClick()
        assertEquals(AppDetailsState.Loading, viewModel.state.value)
        runCurrent()

        viewModel.onInstallClick()
        viewModel.onInstallClick()
        viewModel.onInstallClick()
        advanceTimeBy(150)
        runCurrent()
        assertEquals(InstallState.Installing(5), viewModel.installState)
        advanceTimeBy(2_850)
        runCurrent()
        assertEquals(InstallState.Installed, viewModel.installState)
    }

    private fun createViewModel() = AppDetailsViewModel(
        savedStateHandle = SavedStateHandle(mapOf(APP_ID_ARGUMENT to "quicknote")),
        repository = AppDetailsRepository(object : AppsApi {
            override suspend fun getCatalog(): List<AppDto> = emptyList()

            override suspend fun getAppDetails(id: String): AppDetailsDto {
                assertEquals("quicknote", id)
                return AppDetailsDto(
                    id = id,
                    name = "QuickNote",
                    developer = "QuickSoft",
                    category = "Notes",
                    ageRating = 3,
                    size = 8.2,
                    iconUrl = null,
                    screenshotUrlList = emptyList(),
                    description = "Notes and lists"
                )
            }
        })
    )

    private val AppDetailsViewModel.installState: InstallState
        get() = (state.value as AppDetailsState.Content).installState
}
