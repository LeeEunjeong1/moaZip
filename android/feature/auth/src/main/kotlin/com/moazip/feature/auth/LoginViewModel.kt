package com.moazip.feature.auth

import com.moazip.core.presentation.mvi.MviViewModel

class LoginViewModel : MviViewModel<LoginIntent, LoginState, LoginEffect>(LoginState()) {
    override fun onIntent(intent: LoginIntent) {
        when (intent) {
            LoginIntent.GoogleLoginClicked -> postEffect(LoginEffect.RequestGoogleLogin)
            LoginIntent.InviteCodeClicked -> postEffect(LoginEffect.OpenInviteCode)
        }
    }
}
