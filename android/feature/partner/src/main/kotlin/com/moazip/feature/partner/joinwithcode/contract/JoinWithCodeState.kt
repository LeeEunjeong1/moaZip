package com.moazip.feature.partner.joinwithcode.contract

import com.moazip.core.presentation.mvi.UiState

data class JoinWithCodeState(
    val code: String = "",
    val isJoining: Boolean = false,
    val requiresHouseholdSwitch: Boolean = false,
    val errorMessage: String? = null,
) : UiState {
    val canJoin: Boolean
        get() = code.matches(Regex("[A-Z0-9]{2}-[A-Z0-9]{4}")) &&
            !isJoining &&
            !requiresHouseholdSwitch
    val canSwitchHousehold: Boolean get() = requiresHouseholdSwitch && !isJoining
}
