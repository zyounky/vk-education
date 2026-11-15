package com.example.vkeducation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkeducation.data.RatingsRepository
import com.example.vkeducation.domain.model.AppInfo
import com.example.vkeducation.domain.repository.AppsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface AppListUiState {
    data object Loading : AppListUiState
    data class Loaded(
        val applications: List<AppInfo>,
        val ratings: RatingsUiState = RatingsUiState()
    ) : AppListUiState

    data object Error : AppListUiState
}

data class RatingsUiState(
    val byAppId: Map<String, Float> = emptyMap(),
    val sequentialMillis: Long? = null,
    val parallelMillis: Long? = null,
    val stage: RatingsStage = RatingsStage.Idle
) {
    val isComparing: Boolean
        get() = stage == RatingsStage.Sequential || stage == RatingsStage.Parallel
}

enum class RatingsStage { Idle, Sequential, Parallel, Failed }

sealed interface AppListEvent {
    data class ShowAppInfo(val appName: String) : AppListEvent
}

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val repository: AppsRepository,
    private val ratingsRepository: RatingsRepository
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

    fun compareRatings() {
        val catalog = _uiState.value as? AppListUiState.Loaded ?: return
        if (catalog.applications.isEmpty() || catalog.ratings.isComparing) return

        val appIds = catalog.applications.map { it.id }
        val sequentialStage = catalog.copy(ratings = RatingsUiState(stage = RatingsStage.Sequential))
        if (!_uiState.compareAndSet(catalog, sequentialStage)) return

        viewModelScope.launch {
            var measured = sequentialStage.ratings
            try {
                val sequential = ratingsRepository.loadSequentially(appIds)
                measured = RatingsUiState(
                    byAppId = sequential.byAppId,
                    sequentialMillis = sequential.elapsedMillis,
                    stage = RatingsStage.Parallel
                )
                _uiState.value = catalog.copy(ratings = measured)

                val parallel = ratingsRepository.loadInParallel(appIds)
                _uiState.value = catalog.copy(
                    ratings = measured.copy(
                        byAppId = parallel.byAppId,
                        parallelMillis = parallel.elapsedMillis,
                        stage = RatingsStage.Idle
                    )
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.value = catalog.copy(ratings = measured.copy(stage = RatingsStage.Failed))
            }
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
}
