package com.moazip.feature.budget.editbudget.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.moazip.feature.budget.contract.BudgetAllocationUiModel
import com.moazip.feature.budget.contract.MonthlyBudgetState
import com.moazip.feature.budget.editbudget.contract.EditBudgetIntent
import com.moazip.feature.budget.editbudget.ui.EditBudgetScreen

@Composable
fun EditBudgetRoute(
    initialState: MonthlyBudgetState,
    onSave: (MonthlyBudgetState) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var state by remember(initialState) { mutableStateOf(initialState) }

    fun update(intent: EditBudgetIntent) {
        val memberIndex = when (intent) {
            is EditBudgetIntent.ChangeIncome -> intent.memberIndex
            is EditBudgetIntent.ChangeAllocationName -> intent.memberIndex
            is EditBudgetIntent.ChangeAllocationAmount -> intent.memberIndex
            is EditBudgetIntent.AddAllocation -> intent.memberIndex
            is EditBudgetIntent.RemoveAllocation -> intent.memberIndex
        }
        when (intent) {
            is EditBudgetIntent.ChangeIncome -> state = state.copy(
                members = state.members.mapIndexed { index, member ->
                    if (index == intent.memberIndex) member.copy(income = intent.value.digitsToLong()) else member
                },
            )
            is EditBudgetIntent.ChangeAllocationName -> state = state.updateAllocation(memberIndex, intent.itemIndex) {
                copy(name = intent.value)
            }
            is EditBudgetIntent.ChangeAllocationAmount -> state = state.updateAllocation(memberIndex, intent.itemIndex) {
                copy(amount = intent.value.digitsToLong())
            }
            is EditBudgetIntent.AddAllocation -> state = if (memberIndex == null) {
                state.copy(jointSavings = state.jointSavings + BudgetAllocationUiModel("", 0))
            } else {
                state.copy(members = state.members.mapIndexed { index, member ->
                    if (index == memberIndex) member.copy(allocations = member.allocations + BudgetAllocationUiModel("", 0)) else member
                })
            }
            is EditBudgetIntent.RemoveAllocation -> state = if (memberIndex == null) {
                state.copy(jointSavings = state.jointSavings.filterIndexed { index, _ -> index != intent.itemIndex })
            } else {
                state.copy(members = state.members.mapIndexed { index, member ->
                    if (index == memberIndex) member.copy(
                        allocations = member.allocations.filterIndexed { itemIndex, _ -> itemIndex != intent.itemIndex },
                    ) else member
                })
            }
        }
    }

    EditBudgetScreen(state, ::update, { onSave(state) }, onBack, modifier)
}

private fun String.digitsToLong(): Long = filter(Char::isDigit).toLongOrNull() ?: 0L

private fun MonthlyBudgetState.updateAllocation(
    memberIndex: Int?,
    itemIndex: Int,
    update: BudgetAllocationUiModel.() -> BudgetAllocationUiModel,
): MonthlyBudgetState = if (memberIndex == null) {
    copy(jointSavings = jointSavings.mapIndexed { index, item -> if (index == itemIndex) item.update() else item })
} else {
    copy(members = members.mapIndexed { index, member ->
        if (index == memberIndex) member.copy(
            allocations = member.allocations.mapIndexed { allocationIndex, item ->
                if (allocationIndex == itemIndex) item.update() else item
            },
        ) else member
    })
}
