package com.moazip.feature.dashboard

import androidx.lifecycle.viewModelScope
import com.moazip.core.domain.usecase.ObserveDashboardSummary
import com.moazip.core.presentation.mvi.MviViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val observeDashboardSummary: ObserveDashboardSummary,
) : MviViewModel<DashboardIntent, DashboardState, DashboardEffect>(DashboardState()) {
    private var observeJob: Job? = null

    init {
        observeSummary()
    }

    override fun onIntent(intent: DashboardIntent) {
        when (intent) {
            DashboardIntent.Refresh -> observeSummary()
            DashboardIntent.OpenAssets -> postEffect(DashboardEffect.NavigateToAssets)
        }
    }

    private fun observeSummary() {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            observeDashboardSummary()
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
