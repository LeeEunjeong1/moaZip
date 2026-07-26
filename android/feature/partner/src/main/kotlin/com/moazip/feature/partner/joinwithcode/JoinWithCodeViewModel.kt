package com.moazip.feature.partner.joinwithcode

import com.moazip.feature.partner.joinwithcode.contract.*
import androidx.lifecycle.viewModelScope
import com.moazip.core.domain.usecase.JoinHouseholdWithInviteCodeUseCase
import com.moazip.core.model.JoinHouseholdResult
import com.moazip.core.presentation.mvi.MviViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import com.moazip.core.domain.auth.CurrentUserProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JoinWithCodeViewModel @Inject constructor(
    private val joinHouseholdWithInviteCodeUseCase: JoinHouseholdWithInviteCodeUseCase,
    private val currentUserProvider: CurrentUserProvider,
) : MviViewModel<JoinWithCodeIntent, JoinWithCodeState, JoinWithCodeEffect>(
    JoinWithCodeState(),
) {
    override fun onIntent(intent: JoinWithCodeIntent) {
        when (intent) {
            is JoinWithCodeIntent.CodeChanged -> reduce {
                copy(
                    code = intent.code
                        .filter { it.isLetterOrDigit() || it == '-' }
                        .uppercase(Locale.ROOT)
                        .take(CODE_LENGTH),
                    requiresHouseholdSwitch = false,
                    errorMessage = null,
                )
            }
            JoinWithCodeIntent.JoinClicked -> {
                joinHousehold(replaceExistingHousehold = false)
            }
            JoinWithCodeIntent.SwitchHouseholdClicked -> joinHousehold(replaceExistingHousehold = true)
        }
    }

    private fun joinHousehold(replaceExistingHousehold: Boolean) {
        val currentState = state.value
        val userId = currentUserProvider.userId
        if (!currentState.canJoin && !currentState.canSwitchHousehold) return
        if (userId == null) {
            reduce { copy(errorMessage = "로그인 정보를 확인할 수 없어요. 다시 로그인해 주세요.") }
            return
        }

        viewModelScope.launch {
            reduce { copy(isJoining = true, errorMessage = null) }
            runCatching {
                joinHouseholdWithInviteCodeUseCase(
                    userId = userId,
                    inviteCode = currentState.code,
                    replaceExistingHousehold = replaceExistingHousehold,
                )
            }.onSuccess { result ->
                when (result) {
                    JoinHouseholdResult.Joined,
                    JoinHouseholdResult.AlreadyMemberOfHousehold
                    -> {
                        reduce { copy(isJoining = false, requiresHouseholdSwitch = false) }
                        postEffect(JoinWithCodeEffect.NavigateToDashboard)
                    }
                    JoinHouseholdResult.RequiresHouseholdSwitch -> {
                        reduce {
                            copy(
                                isJoining = false,
                                requiresHouseholdSwitch = true,
                                errorMessage = "이미 다른 우리 집에 참여 중이에요. 이 초대코드로 바꾸시겠어요?",
                            )
                        }
                    }
                }
            }.onFailure {
                reduce {
                    copy(
                        isJoining = false,
                        requiresHouseholdSwitch = false,
                        errorMessage = "초대 코드를 확인할 수 없어요. 다시 확인해 주세요.",
                    )
                }
            }
        }
    }

    private companion object {
        const val CODE_LENGTH = 7
    }
}
