package com.moazip.feature.partner.joinwithcode.contract

import com.moazip.core.presentation.mvi.UiEffect

sealed interface JoinWithCodeEffect : UiEffect {
    data object NavigateToDashboard : JoinWithCodeEffect
}
