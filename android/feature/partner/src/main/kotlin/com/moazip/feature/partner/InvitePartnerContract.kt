package com.moazip.feature.partner

import com.moazip.core.presentation.mvi.UiEffect
import com.moazip.core.presentation.mvi.UiIntent
import com.moazip.core.presentation.mvi.UiState

sealed interface InvitePartnerIntent : UiIntent {
    data object CopyCodeClicked : InvitePartnerIntent
    data object LaterClicked : InvitePartnerIntent
    data object JoinWithCodeClicked : InvitePartnerIntent
}

data class InvitePartnerState(
    val inviteCode: String = "MZ-4821",
    val isCodeCopied: Boolean = false,
) : UiState

sealed interface InvitePartnerEffect : UiEffect {
    data object NavigateToDashboard : InvitePartnerEffect
    data object OpenJoinWithCode : InvitePartnerEffect
}
