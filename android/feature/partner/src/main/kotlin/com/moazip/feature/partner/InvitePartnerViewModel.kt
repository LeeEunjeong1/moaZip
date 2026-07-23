package com.moazip.feature.partner

import com.moazip.core.presentation.mvi.MviViewModel

class InvitePartnerViewModel(
    inviteCode: String,
) : MviViewModel<InvitePartnerIntent, InvitePartnerState, InvitePartnerEffect>(
    InvitePartnerState(inviteCode = inviteCode),
) {
    override fun onIntent(intent: InvitePartnerIntent) {
        when (intent) {
            InvitePartnerIntent.CopyCodeClicked -> reduce { copy(isCodeCopied = true) }
            InvitePartnerIntent.LaterClicked -> postEffect(InvitePartnerEffect.NavigateToDashboard)
            InvitePartnerIntent.JoinWithCodeClicked -> postEffect(InvitePartnerEffect.OpenJoinWithCode)
        }
    }
}
