package com.moazip.feature.partner

import com.moazip.core.presentation.mvi.UiEffect
import com.moazip.core.presentation.mvi.UiIntent
import com.moazip.core.presentation.mvi.UiState

sealed interface CreateHomeIntent : UiIntent {
    data class HomeNameChanged(val name: String) : CreateHomeIntent
    data object CreateClicked : CreateHomeIntent
    data object JoinWithCodeClicked : CreateHomeIntent
}

data class CreateHomeState(
    val homeName: String = "",
    val isCreating: Boolean = false,
    val errorMessage: String? = null,
) : UiState {
    val canCreate: Boolean get() = homeName.isNotBlank() && !isCreating
}

sealed interface CreateHomeEffect : UiEffect {
    data class NavigateToInvitePartner(val inviteCode: String) : CreateHomeEffect
    data object NavigateToJoinWithCode : CreateHomeEffect
}
