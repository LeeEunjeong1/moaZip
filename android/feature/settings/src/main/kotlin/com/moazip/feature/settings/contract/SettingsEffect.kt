package com.moazip.feature.settings.contract

import com.moazip.core.presentation.mvi.UiEffect

sealed interface SettingsEffect : UiEffect {
    data object NavigateToLogin : SettingsEffect
}
