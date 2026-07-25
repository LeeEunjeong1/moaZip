package com.moazip.feature.auth.contract

import com.moazip.core.presentation.mvi.UiState

data class LoginState(
    val isLoading: Boolean = false,
    val error: LoginError? = null,
) : UiState
