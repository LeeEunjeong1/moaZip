package com.moazip.feature.budget.history.contract

import com.moazip.core.presentation.mvi.UiState
import com.moazip.feature.budget.contract.BudgetError
import com.moazip.feature.budget.contract.MonthlyBudgetState

data class BudgetHistoryState(
    val plans: List<MonthlyBudgetState> = emptyList(),
    val isLoading: Boolean = true,
    val error: BudgetError? = null,
) : UiState
