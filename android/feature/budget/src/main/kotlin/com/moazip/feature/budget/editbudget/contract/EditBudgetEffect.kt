package com.moazip.feature.budget.editbudget.contract

import com.moazip.core.presentation.mvi.UiEffect

sealed interface EditBudgetEffect : UiEffect {
    data object Saved : EditBudgetEffect
    data object NavigateBack : EditBudgetEffect
}
