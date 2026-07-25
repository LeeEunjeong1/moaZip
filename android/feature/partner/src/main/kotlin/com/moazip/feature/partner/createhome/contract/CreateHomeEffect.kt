package com.moazip.feature.partner.createhome.contract

import com.moazip.core.presentation.mvi.UiEffect

sealed interface CreateHomeEffect : UiEffect {
    data class NavigateToInvitePartner(val inviteCode: String) : CreateHomeEffect
    data object NavigateToJoinWithCode : CreateHomeEffect
}
