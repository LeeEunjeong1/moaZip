package com.moazip.feature.dashboard

import com.moazip.feature.dashboard.contract.*
import androidx.lifecycle.viewModelScope
import com.moazip.core.domain.usecase.GetLatestInviteCodeUseCase
import com.moazip.core.domain.usecase.ObserveDashboardSummary
import com.moazip.core.presentation.mvi.MviViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val observeDashboardSummary: ObserveDashboardSummary,
    private val getLatestInviteCodeUseCase: GetLatestInviteCodeUseCase,
    private val currentUserIdProvider: () -> String?,
) : MviViewModel<DashboardIntent, DashboardState, DashboardEffect>(DashboardState()) {
    private var observeJob: Job? = null

    init {
        observeSummary()
    }

    override fun onIntent(intent: DashboardIntent) {
        when (intent) {
            DashboardIntent.Refresh -> observeSummary()
            DashboardIntent.OpenAssets -> postEffect(DashboardEffect.NavigateToAssets)
            DashboardIntent.OpenPartnerInvite -> openPartnerInvite()
        }
    }

    private fun openPartnerInvite() {
        if (state.value.isInviteLoading) return
        val userId = currentUserIdProvider() ?: return
        viewModelScope.launch {
            reduce { copy(isInviteLoading = true) }
            runCatching { getLatestInviteCodeUseCase(userId) }
                .onSuccess { inviteCode ->
                    reduce { copy(isInviteLoading = false) }
                    if (inviteCode != null) {
                        postEffect(DashboardEffect.NavigateToPartnerInvite(inviteCode))
                    }
                }
                .onFailure { reduce { copy(isInviteLoading = false) } }
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
