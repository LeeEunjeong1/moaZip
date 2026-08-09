package com.moazip.feature.dashboard.contract

import com.moazip.core.model.DashboardSummary
import com.moazip.core.model.AssetSnapshot
import com.moazip.core.presentation.mvi.UiState

data class DashboardState(
    val isLoading: Boolean = true,
    val summary: DashboardSummary = DashboardSummary(),
    val errorMessage: String? = null,
    val latestSnapshot: AssetSnapshot? = null,
    val assetTrend: List<DashboardAssetTrendPoint> = emptyList(),
) : UiState
