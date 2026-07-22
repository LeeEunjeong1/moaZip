package com.moazip.feature.partner

import com.moazip.core.presentation.mvi.UiEffect
import com.moazip.core.presentation.mvi.UiIntent
import com.moazip.core.presentation.mvi.UiState

sealed interface JoinWithCodeIntent : UiIntent {
    data class CodeChanged(val code: String) : JoinWithCodeIntent
    data object JoinClicked : JoinWithCodeIntent
}

data class JoinWithCodeState(
    val code: String = "",
) : UiState {
    val canJoin: Boolean get() = code.matches(Regex("[A-Z0-9]{2}-[A-Z0-9]{4}"))
}

sealed interface JoinWithCodeEffect : UiEffect {
    data object NavigateToDashboard : JoinWithCodeEffect
}
