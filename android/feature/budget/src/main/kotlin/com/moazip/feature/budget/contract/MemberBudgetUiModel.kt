package com.moazip.feature.budget.contract

data class MemberBudgetUiModel(
    val name: String,
    val income: Long,
    val allocations: List<BudgetAllocationUiModel>,
) {
    val allocatedAmount: Long get() = allocations.sumOf(BudgetAllocationUiModel::amount)
    val remainingAmount: Long get() = income - allocatedAmount
}
