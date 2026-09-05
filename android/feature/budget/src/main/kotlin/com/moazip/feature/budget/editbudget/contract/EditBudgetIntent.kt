package com.moazip.feature.budget.editbudget.contract

sealed interface EditBudgetIntent {
    data class ChangeIncome(val memberIndex: Int, val value: String) : EditBudgetIntent
    data class ChangeAllocationName(val memberIndex: Int?, val itemIndex: Int, val value: String) : EditBudgetIntent
    data class ChangeAllocationAmount(val memberIndex: Int?, val itemIndex: Int, val value: String) : EditBudgetIntent
    data class AddAllocation(val memberIndex: Int?) : EditBudgetIntent
    data class RemoveAllocation(val memberIndex: Int?, val itemIndex: Int) : EditBudgetIntent
}
