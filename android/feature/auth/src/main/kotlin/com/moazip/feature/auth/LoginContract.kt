package com.moazip.feature.auth

import com.moazip.core.presentation.mvi.UiEffect
import com.moazip.core.presentation.mvi.UiIntent
import com.moazip.core.presentation.mvi.UiState

sealed interface LoginIntent : UiIntent {
    data object GoogleLoginClicked : LoginIntent
    data class GoogleLoginCompleted(val outcome: GoogleLoginOutcome) : LoginIntent
}

data class LoginState(
    val isLoading: Boolean = false,
    val error: LoginError? = null,
) : UiState

sealed interface LoginEffect : UiEffect {
    data object RequestGoogleLogin : LoginEffect
    data object NavigateToPartnerSetup : LoginEffect
}

sealed interface GoogleLoginOutcome {
    data object Success : GoogleLoginOutcome
    data object Cancelled : GoogleLoginOutcome
    data object Failure : GoogleLoginOutcome
}

enum class LoginError {
    GoogleLoginFailed,
}
