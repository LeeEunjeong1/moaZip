package com.moazip.feature.auth.contract

import com.moazip.core.presentation.mvi.UiIntent

sealed interface LoginIntent : UiIntent {
    data object GoogleLoginClicked : LoginIntent
    data class GoogleLoginCompleted(val outcome: GoogleLoginOutcome) : LoginIntent
}
