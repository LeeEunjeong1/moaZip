package com.moazip.feature.dashboard

import com.moazip.feature.dashboard.contract.*
import androidx.lifecycle.viewModelScope
import com.moazip.core.domain.usecase.ObserveDashboardSummary
import com.moazip.core.domain.usecase.ObserveAssetSnapshotsUseCase
import com.moazip.core.presentation.mvi.MviViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.moazip.core.domain.auth.CurrentUserProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val observeDashboardSummary: ObserveDashboardSummary,
    private val observeAssetSnapshotsUseCase: ObserveAssetSnapshotsUseCase,
    private val currentUserProvider: CurrentUserProvider,
) : MviViewModel<DashboardIntent, DashboardState, DashboardEffect>(DashboardState()) {
    private var observeJob: Job? = null
    private var observeSnapshotsJob: Job? = null

    init {
        observeSummary()
        observeLatestSnapshot()
    }

    override fun onIntent(intent: DashboardIntent) {
        when (intent) {
            DashboardIntent.Refresh -> {
                observeSummary()
                observeLatestSnapshot()
            }
            DashboardIntent.OpenAssets -> postEffect(DashboardEffect.NavigateToAssets)
        }
    }

    private fun observeLatestSnapshot() {
        observeSnapshotsJob?.cancel()
        val userId = currentUserProvider.userId ?: return
        observeSnapshotsJob = viewModelScope.launch {
            observeAssetSnapshotsUseCase(userId)
                .catch { /* Current asset summary can still be displayed. */ }
                .collect { snapshots ->
                    reduce { copy(latestSnapshot = snapshots.maxByOrNull { it.monthKey }) }
                }
        }
    }

    private fun observeSummary() {
        observeJob?.cancel()
        val userId = currentUserProvider.userId
        if (userId == null) {
            reduce {
                copy(isLoading = false, errorMessage = "로그인 정보를 확인할 수 없어요.")
            }
            return
        }
        observeJob = viewModelScope.launch {
            observeDashboardSummary(userId)
                .onStart { reduce { copy(isLoading = true, errorMessage = null) } }
                .catch { error ->
                    val message = error.message ?: "자산 정보를 불러오지 못했어요."
                    reduce { copy(isLoading = false, errorMessage = message) }
                    postEffect(DashboardEffect.ShowMessage(message))
                }
                .collect { summary ->
                    reduce { copy(isLoading = false, summary = summary, errorMessage = null) }
                }
        }
    }
}
