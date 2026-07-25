package com.moazip.feature.partner.joinwithcode.contract

import com.moazip.core.presentation.mvi.UiIntent

sealed interface JoinWithCodeIntent : UiIntent {
    data class CodeChanged(val code: String) : JoinWithCodeIntent
    data object JoinClicked : JoinWithCodeIntent
    data object SwitchHouseholdClicked : JoinWithCodeIntent
}
