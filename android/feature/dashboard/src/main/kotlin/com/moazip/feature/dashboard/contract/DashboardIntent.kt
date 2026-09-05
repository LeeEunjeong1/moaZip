package com.moazip.feature.dashboard.contract

import com.moazip.core.presentation.mvi.UiIntent

sealed interface DashboardIntent : UiIntent {
    data object Refresh : DashboardIntent
    data object OpenAssets : DashboardIntent
    data object OpenRecords : DashboardIntent
    data object OpenMonthlyBudget : DashboardIntent
}
