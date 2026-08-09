package com.moazip.feature.settings.contract

import com.moazip.core.presentation.mvi.UiState

data class SettingsState(
    val householdName: String = "",
    val members: List<SettingsMemberUiModel> = emptyList(),
    val inviteCode: String? = null,
    val canReissueInviteCode: Boolean = false,
    val isLoading: Boolean = true,
    val isReissuingInviteCode: Boolean = false,
    val error: SettingsError? = null,
) : UiState
