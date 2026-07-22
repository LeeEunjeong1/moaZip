package com.moazip.feature.dashboard

import com.moazip.core.model.DashboardSummary
import com.moazip.core.presentation.mvi.UiEffect
import com.moazip.core.presentation.mvi.UiIntent
import com.moazip.core.presentation.mvi.UiState

sealed interface DashboardIntent : UiIntent {
    data object Refresh : DashboardIntent
    data object OpenAssets : DashboardIntent
}

data class DashboardState(
    val isLoading: Boolean = true,
    val summary: DashboardSummary = DashboardSummary(),
    val errorMessage: String? = null,
) : UiState

sealed interface DashboardEffect : UiEffect {
    data object NavigateToAssets : DashboardEffect
    data class ShowMessage(val message: String) : DashboardEffect
}
