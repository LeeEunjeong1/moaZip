package com.moazip.feature.partner.invitepartner.contract

import com.moazip.core.presentation.mvi.UiEffect

sealed interface InvitePartnerEffect : UiEffect {
    data object NavigateToDashboard : InvitePartnerEffect
    data object OpenJoinWithCode : InvitePartnerEffect
}
