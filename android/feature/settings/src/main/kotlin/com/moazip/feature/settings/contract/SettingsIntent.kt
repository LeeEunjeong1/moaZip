package com.moazip.feature.settings.contract

import com.moazip.core.presentation.mvi.UiIntent

sealed interface SettingsIntent : UiIntent {
    data object ReissueInviteCodeClicked : SettingsIntent
    data object LogoutClicked : SettingsIntent
}
