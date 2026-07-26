package com.moazip.feature.auth

import com.moazip.feature.auth.contract.*
import com.moazip.core.presentation.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() :
    MviViewModel<LoginIntent, LoginState, LoginEffect>(LoginState()) {
    override fun onIntent(intent: LoginIntent) {
        when (intent) {
            LoginIntent.GoogleLoginClicked -> {
                if (state.value.isLoading) return
                reduce { copy(isLoading = true, error = null) }
                postEffect(LoginEffect.RequestGoogleLogin)
            }
            is LoginIntent.GoogleLoginCompleted -> handleGoogleLoginOutcome(intent.outcome)
        }
    }

    private fun handleGoogleLoginOutcome(outcome: GoogleLoginOutcome) {
        when (outcome) {
            GoogleLoginOutcome.Success -> {
                reduce { copy(isLoading = false, error = null) }
                postEffect(LoginEffect.NavigateToPartnerSetup)
            }
            GoogleLoginOutcome.Cancelled -> reduce { copy(isLoading = false, error = null) }
            GoogleLoginOutcome.Failure -> reduce {
                copy(isLoading = false, error = LoginError.GoogleLoginFailed)
            }
        }
    }
}
