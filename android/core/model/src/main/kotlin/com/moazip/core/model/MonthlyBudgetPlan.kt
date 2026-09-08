package com.moazip.core.model

data class MonthlyBudgetPlan(
    val monthId: String,
    val members: List<MemberBudget>,
    val jointAllocations: List<BudgetAllocation>,
    val jointSavings: List<BudgetAllocation>,
)
