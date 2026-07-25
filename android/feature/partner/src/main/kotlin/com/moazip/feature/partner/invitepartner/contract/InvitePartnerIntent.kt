package com.moazip.feature.partner.invitepartner.contract

import com.moazip.core.presentation.mvi.UiIntent

sealed interface InvitePartnerIntent : UiIntent {
    data object CopyCodeClicked : InvitePartnerIntent
    data object ReissueCodeClicked : InvitePartnerIntent
    data object LaterClicked : InvitePartnerIntent
    data object JoinWithCodeClicked : InvitePartnerIntent
}
