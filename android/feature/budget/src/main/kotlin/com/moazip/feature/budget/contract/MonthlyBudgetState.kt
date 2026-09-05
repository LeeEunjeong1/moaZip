package com.moazip.feature.budget.contract

data class MonthlyBudgetState(
    val monthLabel: String,
    val members: List<MemberBudgetUiModel>,
    val jointSavings: List<BudgetAllocationUiModel>,
) {
    val totalIncome: Long get() = members.sumOf(MemberBudgetUiModel::income)
    val totalAllocation: Long
        get() = members.sumOf(MemberBudgetUiModel::allocatedAmount) +
            jointSavings.sumOf(BudgetAllocationUiModel::amount)
    val availableSaving: Long get() = totalIncome - totalAllocation
    val savingRate: Double
        get() = if (totalIncome == 0L) 0.0 else availableSaving.toDouble() / totalIncome * 100
}
