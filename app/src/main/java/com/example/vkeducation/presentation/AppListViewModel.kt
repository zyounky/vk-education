package com.example.vkeducation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkeducation.data.AppsRepository
import com.example.vkeducation.data.LocalAppsRepository
import com.example.vkeducation.model.AppInfo
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
    repository: AppsRepository = LocalAppsRepository
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
}
