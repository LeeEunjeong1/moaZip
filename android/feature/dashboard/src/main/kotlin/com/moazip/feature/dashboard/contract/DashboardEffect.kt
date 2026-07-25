package com.moazip.feature.dashboard.contract

import com.moazip.core.presentation.mvi.UiEffect

sealed interface DashboardEffect : UiEffect {
    data object NavigateToAssets : DashboardEffect
    data class NavigateToPartnerInvite(val inviteCode: String) : DashboardEffect
    data class ShowMessage(val message: String) : DashboardEffect
}
