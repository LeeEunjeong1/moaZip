package com.moazip.core.model

data class MonthlyBudgetPlan(
    val monthId: String,
    val members: List<MemberBudget>,
    val jointSavings: List<BudgetAllocation>,
)
