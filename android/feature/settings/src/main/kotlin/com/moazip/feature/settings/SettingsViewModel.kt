package com.moazip.feature.settings

import androidx.lifecycle.viewModelScope
import com.moazip.core.domain.auth.CurrentUserProvider
import com.moazip.core.domain.usecase.GetHouseholdDetailsUseCase
import com.moazip.core.domain.usecase.ReissueInviteCodeUseCase
import com.moazip.core.domain.usecase.SignOutUseCase
import com.moazip.core.model.HouseholdMemberRole
import com.moazip.core.presentation.mvi.MviViewModel
import com.moazip.feature.settings.contract.SettingsEffect
import com.moazip.feature.settings.contract.SettingsError
import com.moazip.feature.settings.contract.SettingsIntent
import com.moazip.feature.settings.contract.SettingsMemberRole
import com.moazip.feature.settings.contract.SettingsMemberUiModel
import com.moazip.feature.settings.contract.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getHouseholdDetailsUseCase: GetHouseholdDetailsUseCase,
    private val reissueInviteCodeUseCase: ReissueInviteCodeUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val currentUserProvider: CurrentUserProvider,
) : MviViewModel<SettingsIntent, SettingsState, SettingsEffect>(SettingsState()) {

    init { loadSettings() }

    override fun onIntent(intent: SettingsIntent) {
        when (intent) {
            SettingsIntent.RetryClicked -> loadSettings()
            SettingsIntent.ReissueInviteCodeClicked -> reissueInviteCode()
            SettingsIntent.LogoutClicked -> logout()
        }
    }

    private fun loadSettings() {
        val userId = currentUserProvider.userId ?: run {
            reduce { copy(isLoading = false, error = SettingsError.UNAUTHENTICATED) }
            return
        }
        reduce { copy(isLoading = true, error = null) }
        viewModelScope.launch {
            runCatching { getHouseholdDetailsUseCase(userId) }
                .onSuccess { household ->
                    reduce {
                        copy(
                            householdName = household.name,
                            inviteCode = household.inviteCode,
                            canReissueInviteCode = household.members.any { member ->
                                member.userId == userId && member.role == HouseholdMemberRole.OWNER
                            },
                            members = household.members.map { member ->
                                SettingsMemberUiModel(
                                    id = member.userId,
                                    name = member.displayName.orEmpty(),
                                    role = if (member.role == HouseholdMemberRole.OWNER) {
                                        SettingsMemberRole.OWNER
                                    } else {
                                        SettingsMemberRole.MEMBER
                                    },
                                )
                            },
                            isLoading = false,
                            error = null,
                        )
                    }
                }
                .onFailure { reduce { copy(isLoading = false, error = SettingsError.LOAD_FAILED) } }
        }
    }

    private fun reissueInviteCode() {
        if (state.value.isReissuingInviteCode) return
        if (!state.value.canReissueInviteCode) return
        val userId = currentUserProvider.userId ?: return
        val currentCode = state.value.inviteCode ?: run {
            reduce { copy(error = SettingsError.REISSUE_FAILED) }
            return
        }
        reduce { copy(isReissuingInviteCode = true, error = null) }
        viewModelScope.launch {
            runCatching { reissueInviteCodeUseCase(userId, currentCode) }
                .onSuccess { code ->
                    reduce { copy(inviteCode = code, isReissuingInviteCode = false, error = null) }
                }
                .onFailure {
                    reduce { copy(isReissuingInviteCode = false, error = SettingsError.REISSUE_FAILED) }
                }
        }
    }

    private fun logout() {
        signOutUseCase()
        postEffect(SettingsEffect.NavigateToLogin)
    }
}
