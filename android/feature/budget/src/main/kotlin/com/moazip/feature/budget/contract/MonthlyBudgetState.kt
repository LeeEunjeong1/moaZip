package com.moazip.feature.budget.contract

import com.moazip.core.presentation.mvi.UiState

data class MonthlyBudgetState(
    val monthId: String = "",
    val monthLabel: String = "",
    val members: List<MemberBudgetUiModel> = emptyList(),
    val jointAllocations: List<BudgetAllocationUiModel> = emptyList(),
    val jointSavings: List<BudgetAllocationUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isCopiedFromPreviousMonth: Boolean = false,
    val error: BudgetError? = null,
) : UiState {
    val totalIncome: Long get() = members.sumOf(MemberBudgetUiModel::income)
    val totalAllocation: Long
        get() = members.sumOf(MemberBudgetUiModel::budgetAmount) + jointAllocations.sumOf(BudgetAllocationUiModel::amount)
    val plannedSaving: Long
        get() = members.sumOf(MemberBudgetUiModel::plannedSavingAmount) + jointSavings.sumOf(BudgetAllocationUiModel::amount)
    val remainingSaving: Long get() = totalIncome - totalAllocation - plannedSaving
    val availableSaving: Long get() = plannedSaving + remainingSaving
    val savingRate: Double
        get() = if (totalIncome == 0L) 0.0 else availableSaving.toDouble() / totalIncome * 100
}
