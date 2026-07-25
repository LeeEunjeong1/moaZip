package com.moazip.feature.partner.invitepartner.contract

import com.moazip.core.presentation.mvi.UiState

data class InvitePartnerState(
    val inviteCode: String,
    val isCodeCopied: Boolean = false,
    val isReissuing: Boolean = false,
    val reissueError: Boolean = false,
) : UiState
