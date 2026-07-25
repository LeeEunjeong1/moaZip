package com.moazip.feature.partner.createhome.contract

import com.moazip.core.presentation.mvi.UiIntent

sealed interface CreateHomeIntent : UiIntent {
    data class HomeNameChanged(val name: String) : CreateHomeIntent
    data object CreateClicked : CreateHomeIntent
    data object JoinWithCodeClicked : CreateHomeIntent
}
