package com.moazip.core.model

data class MemberBudget(
    val name: String,
    val income: Long,
    val budgetAllocations: List<BudgetAllocation>,
    val savings: List<BudgetAllocation>,
)
