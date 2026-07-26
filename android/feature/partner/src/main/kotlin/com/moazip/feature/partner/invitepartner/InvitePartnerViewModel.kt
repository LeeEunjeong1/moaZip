package com.moazip.feature.partner.invitepartner

import com.moazip.feature.partner.invitepartner.contract.*
import com.moazip.core.presentation.mvi.MviViewModel
import com.moazip.core.domain.usecase.ReissueInviteCodeUseCase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.SavedStateHandle
import com.moazip.core.domain.auth.CurrentUserProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvitePartnerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reissueInviteCodeUseCase: ReissueInviteCodeUseCase,
    private val currentUserProvider: CurrentUserProvider,
) : MviViewModel<InvitePartnerIntent, InvitePartnerState, InvitePartnerEffect>(
    InvitePartnerState(
        inviteCode = savedStateHandle.get<String>(INVITE_CODE_ARGUMENT).orEmpty(),
    ),
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
        val ownerUserId = currentUserProvider.userId ?: run {
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

    private companion object {
        const val INVITE_CODE_ARGUMENT = "inviteCode"
    }
}
