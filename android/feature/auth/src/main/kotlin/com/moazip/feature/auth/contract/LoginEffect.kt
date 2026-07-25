package com.moazip.feature.auth.contract

import com.moazip.core.presentation.mvi.UiEffect

sealed interface LoginEffect : UiEffect {
    data object RequestGoogleLogin : LoginEffect
    data object NavigateToPartnerSetup : LoginEffect
}
