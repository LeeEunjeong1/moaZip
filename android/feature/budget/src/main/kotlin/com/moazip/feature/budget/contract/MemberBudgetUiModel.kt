package com.moazip.feature.budget.contract

data class MemberBudgetUiModel(
    val name: String,
    val income: Long,
    val budgetAllocations: List<BudgetAllocationUiModel>,
    val savings: List<BudgetAllocationUiModel>,
) {
    val budgetAmount: Long get() = budgetAllocations.sumOf(BudgetAllocationUiModel::amount)
    val plannedSavingAmount: Long get() = savings.sumOf(BudgetAllocationUiModel::amount)
    val remainingAmount: Long get() = income - budgetAmount - plannedSavingAmount
}
