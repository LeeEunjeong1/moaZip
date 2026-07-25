package com.moazip.feature.partner.createhome.contract

import com.moazip.core.presentation.mvi.UiState

data class CreateHomeState(
    val homeName: String = "",
    val isCreating: Boolean = false,
    val errorMessage: String? = null,
) : UiState {
    val canCreate: Boolean get() = homeName.isNotBlank() && !isCreating
}
