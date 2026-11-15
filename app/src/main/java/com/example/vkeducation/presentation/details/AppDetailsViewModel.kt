package com.example.vkeducation.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkeducation.data.AppDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal const val APP_ID_ARGUMENT = "appId"

@HiltViewModel
class AppDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: AppDetailsRepository
) : ViewModel() {
    private val appId = checkNotNull(savedStateHandle.get<String>(APP_ID_ARGUMENT))
    private val _state = MutableStateFlow<AppDetailsState>(AppDetailsState.Loading)
    val state = _state.asStateFlow()
    private var installJob: Job? = null

    init {
        loadDetails()
    }

    fun retry() {
        if (_state.compareAndSet(AppDetailsState.Error, AppDetailsState.Loading)) {
            loadDetails()
        }
    }

    private fun loadDetails() {
        viewModelScope.launch {
            _state.value = try {
                AppDetailsState.Content(repository.loadAppDetails(appId))
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                AppDetailsState.Error
            }
        }
    }

    fun onInstallClick() {
        val content = _state.value as? AppDetailsState.Content ?: return
        when (content.installState) {
            InstallState.NotInstalled -> {
                _state.value = content.copy(installState = InstallState.Installing(0))
                installJob = viewModelScope.launch { emulateInstall() }
            }
            is InstallState.Installing -> {
                installJob?.cancel()
                installJob = null
                _state.value = content.copy(installState = InstallState.NotInstalled)
            }
            InstallState.Installed -> Unit
        }
    }

    private suspend fun emulateInstall() {
        for (progress in 5..100 step 5) {
            delay(150)
            updateInstallState(InstallState.Installing(progress))
        }
        updateInstallState(InstallState.Installed)
        installJob = null
    }

    private fun updateInstallState(installState: InstallState) {
        _state.update { state ->
            if (state is AppDetailsState.Content) state.copy(installState = installState) else state
        }
    }
}
