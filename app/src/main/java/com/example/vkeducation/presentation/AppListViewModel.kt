package com.example.vkeducation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vkeducation.domain.model.AppInfo
import com.example.vkeducation.domain.repository.AppsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class AppListUiState(val applications: List<AppInfo>)

sealed interface AppListEvent {
    data class ShowAppInfo(val appName: String) : AppListEvent
}

class AppListViewModel(
    repository: AppsRepository
) : ViewModel() {
    // Local data is loaded once for this ViewModel, independently of UI collectors.
    private val _uiState = MutableStateFlow(AppListUiState(repository.loadApps()))
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<AppListEvent>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

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
