package com.moazip.feature.settings.contract

import com.moazip.core.presentation.mvi.UiState

data class SettingsState(
    val householdName: String = "",
    val members: List<SettingsMemberUiModel> = emptyList(),
) : UiState
