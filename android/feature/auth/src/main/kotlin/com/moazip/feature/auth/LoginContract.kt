package com.moazip.feature.auth

import com.moazip.core.presentation.mvi.UiEffect
import com.moazip.core.presentation.mvi.UiIntent
import com.moazip.core.presentation.mvi.UiState

sealed interface LoginIntent : UiIntent {
    data object GoogleLoginClicked : LoginIntent
    data object InviteCodeClicked : LoginIntent
}

data class LoginState(
    val isLoading: Boolean = false,
) : UiState

sealed interface LoginEffect : UiEffect {
    data object RequestGoogleLogin : LoginEffect
    data object OpenInviteCode : LoginEffect
}
