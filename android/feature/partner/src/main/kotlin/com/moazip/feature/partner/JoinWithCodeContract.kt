package com.moazip.feature.partner

import com.moazip.core.presentation.mvi.UiEffect
import com.moazip.core.presentation.mvi.UiIntent
import com.moazip.core.presentation.mvi.UiState

sealed interface JoinWithCodeIntent : UiIntent {
    data class CodeChanged(val code: String) : JoinWithCodeIntent
    data object JoinClicked : JoinWithCodeIntent
    data object SwitchHouseholdClicked : JoinWithCodeIntent
}

data class JoinWithCodeState(
    val code: String = "",
    val isJoining: Boolean = false,
    val requiresHouseholdSwitch: Boolean = false,
    val errorMessage: String? = null,
) : UiState {
    val canJoin: Boolean get() = code.matches(Regex("[A-Z0-9]{2}-[A-Z0-9]{4}")) && !isJoining && !requiresHouseholdSwitch
    val canSwitchHousehold: Boolean get() = requiresHouseholdSwitch && !isJoining
}

sealed interface JoinWithCodeEffect : UiEffect {
    data object NavigateToDashboard : JoinWithCodeEffect
}
