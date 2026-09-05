package com.moazip.feature.budget.contract

import com.moazip.core.presentation.mvi.UiState

data class MonthlyBudgetState(
    val monthId: String = "",
    val monthLabel: String = "",
    val members: List<MemberBudgetUiModel> = emptyList(),
    val jointSavings: List<BudgetAllocationUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
) : UiState {
    val totalIncome: Long get() = members.sumOf(MemberBudgetUiModel::income)
    val totalAllocation: Long
        get() = members.sumOf(MemberBudgetUiModel::allocatedAmount) +
            jointSavings.sumOf(BudgetAllocationUiModel::amount)
    val availableSaving: Long get() = totalIncome - totalAllocation
    val savingRate: Double
        get() = if (totalIncome == 0L) 0.0 else availableSaving.toDouble() / totalIncome * 100
}
