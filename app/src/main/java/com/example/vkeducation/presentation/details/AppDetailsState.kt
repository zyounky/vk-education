package com.example.vkeducation.presentation.details

import com.example.vkeducation.domain.model.AppDetails

sealed interface AppDetailsState {
    data object Loading : AppDetailsState
    data object Error : AppDetailsState
    data class Content(
        val app: AppDetails,
        val installState: InstallState = InstallState.NotInstalled
    ) : AppDetailsState
}

sealed interface InstallState {
    data object NotInstalled : InstallState
    data class Installing(val progress: Int) : InstallState
    data object Installed : InstallState
}
