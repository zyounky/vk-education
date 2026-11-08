package com.example.vkeducation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vkeducation.domain.model.AppInfo
import com.example.vkeducation.domain.repository.AppsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface AppListUiState {
    data object Loading : AppListUiState
    data class Loaded(val applications: List<AppInfo>) : AppListUiState
    data object Error : AppListUiState
}

sealed interface AppListEvent {
    data class ShowAppInfo(val appName: String) : AppListEvent
}

class AppListViewModel(
    private val repository: AppsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<AppListUiState>(AppListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<AppListEvent>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    init {
        loadApps()
    }

    fun retry() {
        if (_uiState.compareAndSet(AppListUiState.Error, AppListUiState.Loading)) {
            loadApps()
        }
    }

    private fun loadApps() {
        viewModelScope.launch {
            _uiState.value = try {
                AppListUiState.Loaded(repository.loadApps())
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                AppListUiState.Error
            }
        }
    }

    fun onAppLogoClicked(app: AppInfo) {
        viewModelScope.launch {
            eventChannel.send(AppListEvent.ShowAppInfo(app.name))
        }
    }

    override fun onCleared() {
        eventChannel.close()
    }

    companion object {
        fun factory(repository: AppsRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { AppListViewModel(repository) }
        }
    }
}
