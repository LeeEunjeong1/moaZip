package com.moazip.feature.partner

import androidx.lifecycle.viewModelScope
import com.moazip.core.domain.usecase.CreateHouseholdUseCase
import com.moazip.core.presentation.mvi.MviViewModel
import kotlinx.coroutines.launch

class CreateHomeViewModel(
    private val createHouseholdUseCase: CreateHouseholdUseCase,
    private val currentUserIdProvider: () -> String?,
) : MviViewModel<CreateHomeIntent, CreateHomeState, CreateHomeEffect>(
    CreateHomeState(),
) {
    override fun onIntent(intent: CreateHomeIntent) {
        when (intent) {
            is CreateHomeIntent.HomeNameChanged -> reduce {
                copy(homeName = intent.name.take(MAX_NAME_LENGTH), errorMessage = null)
            }
            CreateHomeIntent.CreateClicked -> {
                createHousehold()
            }
            CreateHomeIntent.JoinWithCodeClicked -> postEffect(CreateHomeEffect.NavigateToJoinWithCode)
        }
    }

    private fun createHousehold() {
        val currentState = state.value
        val ownerUserId = currentUserIdProvider()
        if (!currentState.canCreate) return
        if (ownerUserId == null) {
            reduce { copy(errorMessage = "로그인 정보를 확인할 수 없어요. 다시 로그인해 주세요.") }
            return
        }

        viewModelScope.launch {
            reduce { copy(isCreating = true, errorMessage = null) }
            runCatching {
                createHouseholdUseCase(
                    ownerUserId = ownerUserId,
                    householdName = currentState.homeName,
                )
            }.onSuccess { result ->
                reduce { copy(isCreating = false) }
                postEffect(CreateHomeEffect.NavigateToInvitePartner(result.inviteCode))
            }.onFailure {
                reduce {
                    copy(
                        isCreating = false,
                        errorMessage = "우리 집을 만들지 못했어요. 잠시 후 다시 시도해 주세요.",
                    )
                }
            }
        }
    }

    private companion object {
        const val MAX_NAME_LENGTH = 20
    }
}
