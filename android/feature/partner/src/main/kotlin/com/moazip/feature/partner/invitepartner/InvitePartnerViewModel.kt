package com.moazip.feature.partner.invitepartner

import com.moazip.feature.partner.invitepartner.contract.*
import com.moazip.core.presentation.mvi.MviViewModel
import com.moazip.core.domain.usecase.ReissueInviteCodeUseCase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class InvitePartnerViewModel(
    inviteCode: String,
    private val reissueInviteCodeUseCase: ReissueInviteCodeUseCase,
    private val currentUserIdProvider: () -> String?,
) : MviViewModel<InvitePartnerIntent, InvitePartnerState, InvitePartnerEffect>(
    InvitePartnerState(inviteCode = inviteCode),
) {
    override fun onIntent(intent: InvitePartnerIntent) {
        when (intent) {
            InvitePartnerIntent.CopyCodeClicked -> reduce { copy(isCodeCopied = true) }
            InvitePartnerIntent.ReissueCodeClicked -> reissueCode()
            InvitePartnerIntent.LaterClicked -> postEffect(InvitePartnerEffect.NavigateToDashboard)
            InvitePartnerIntent.JoinWithCodeClicked -> postEffect(InvitePartnerEffect.OpenJoinWithCode)
        }
    }

    private fun reissueCode() {
        if (state.value.isReissuing) return
        val ownerUserId = currentUserIdProvider() ?: run {
            reduce { copy(reissueError = true) }
            return
        }
        val currentCode = state.value.inviteCode
        reduce { copy(isReissuing = true, reissueError = false) }
        viewModelScope.launch {
            runCatching {
                reissueInviteCodeUseCase(
                    ownerUserId = ownerUserId,
                    currentInviteCode = currentCode,
                )
            }.onSuccess { newCode ->
                reduce {
                    copy(
                        inviteCode = newCode,
                        isCodeCopied = false,
                        isReissuing = false,
                        reissueError = false,
                    )
                }
            }.onFailure {
                reduce { copy(isReissuing = false, reissueError = true) }
            }
        }
    }
}
