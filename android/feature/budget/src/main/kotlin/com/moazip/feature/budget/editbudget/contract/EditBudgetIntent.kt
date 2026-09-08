package com.moazip.feature.budget.editbudget.contract

import com.moazip.core.presentation.mvi.UiIntent

sealed interface EditBudgetIntent : UiIntent {
    data class ChangeIncome(val memberIndex: Int, val value: String) : EditBudgetIntent
    data class ChangeAllocationName(val memberIndex: Int?, val section: BudgetItemSection, val itemIndex: Int, val value: String) : EditBudgetIntent
    data class ChangeAllocationAmount(val memberIndex: Int?, val section: BudgetItemSection, val itemIndex: Int, val value: String) : EditBudgetIntent
    data class AddAllocation(val memberIndex: Int?, val section: BudgetItemSection) : EditBudgetIntent
    data class RemoveAllocation(val memberIndex: Int?, val section: BudgetItemSection, val itemIndex: Int) : EditBudgetIntent
    data object SaveClicked : EditBudgetIntent
    data object CancelClicked : EditBudgetIntent
}
