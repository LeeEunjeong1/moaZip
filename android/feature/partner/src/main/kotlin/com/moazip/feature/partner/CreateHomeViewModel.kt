package com.moazip.feature.partner

import com.moazip.core.presentation.mvi.MviViewModel

class CreateHomeViewModel : MviViewModel<CreateHomeIntent, CreateHomeState, CreateHomeEffect>(
    CreateHomeState(),
) {
    override fun onIntent(intent: CreateHomeIntent) {
        when (intent) {
            is CreateHomeIntent.HomeNameChanged -> reduce { copy(homeName = intent.name.take(MAX_NAME_LENGTH)) }
            CreateHomeIntent.CreateClicked -> {
                if (state.value.canCreate) postEffect(CreateHomeEffect.NavigateToInvitePartner)
            }
            CreateHomeIntent.JoinWithCodeClicked -> postEffect(CreateHomeEffect.NavigateToJoinWithCode)
        }
    }

    private companion object {
        const val MAX_NAME_LENGTH = 20
    }
}
