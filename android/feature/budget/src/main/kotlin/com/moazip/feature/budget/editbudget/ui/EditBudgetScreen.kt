package com.moazip.feature.budget.editbudget.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipButton
import com.moazip.core.ui.component.MoaZipOutlinedButton
import com.moazip.core.ui.component.MoaZipTextField
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.budget.contract.BudgetAllocationUiModel
import com.moazip.feature.budget.contract.MonthlyBudgetState
import com.moazip.feature.budget.editbudget.contract.EditBudgetIntent
import com.moazip.feature.budget.editbudget.contract.BudgetItemSection
import com.moazip.feature.budget.R
import com.moazip.feature.budget.contract.BudgetError
import com.moazip.feature.budget.ui.formatBudgetMonth
import java.text.NumberFormat
import java.util.Locale

@Composable
fun EditBudgetScreen(
    state: MonthlyBudgetState,
    onIntent: (EditBudgetIntent) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(MoaZipPalette.Cream50).imePadding()) {
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 24.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            item {
                TextButton(onClick = onBack, contentPadding = PaddingValues(0.dp)) { Text(stringResource(R.string.budget_edit_back)) }
                Text(stringResource(R.string.budget_edit_title), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(stringResource(R.string.budget_edit_description, formatBudgetMonth(state.monthId)), color = MoaZipPalette.Gray500)
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(stringResource(R.string.budget_expected_saving), color = MoaZipPalette.Gray500)
                    Text(state.availableSaving.asWon(), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(stringResource(R.string.budget_edit_summary, state.totalAllocation.asWon(), state.plannedSaving.asWon()))
                    Text(stringResource(R.string.budget_remaining_money, state.remainingSaving.asWon()), color = MoaZipPalette.Green600)
                }
            }
            state.error?.let { error ->
                item { Text(stringResource(error.stringRes()), color = MaterialTheme.colorScheme.error) }
            }
            itemsIndexed(state.members, key = { _, member -> member.name }) { memberIndex, member ->
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(stringResource(R.string.budget_member_title, member.name), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(stringResource(R.string.budget_salary), fontWeight = FontWeight.SemiBold)
                    AmountField(member.income) { onIntent(EditBudgetIntent.ChangeIncome(memberIndex, it)) }
                    EditAllocationSection(
                        title = stringResource(R.string.budget_allocation_title),
                        description = stringResource(R.string.budget_allocation_edit_description),
                        items = member.budgetAllocations,
                        memberIndex = memberIndex,
                        section = BudgetItemSection.BUDGET,
                        containerColor = MoaZipPalette.Gray50,
                        onIntent = onIntent,
                    )
                    EditAllocationSection(
                        title = stringResource(R.string.budget_saving_plan_title),
                        description = stringResource(R.string.budget_saving_plan_description),
                        items = member.savings,
                        memberIndex = memberIndex,
                        section = BudgetItemSection.SAVING,
                        containerColor = MoaZipPalette.Yellow50,
                        onIntent = onIntent,
                    )
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(stringResource(R.string.budget_joint_items), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    EditAllocationSection(stringResource(R.string.budget_joint_budget), stringResource(R.string.budget_joint_budget_description), state.jointAllocations, null, BudgetItemSection.BUDGET, MoaZipPalette.Gray50, onIntent)
                    EditAllocationSection(stringResource(R.string.budget_joint_saving), stringResource(R.string.budget_joint_saving_description), state.jointSavings, null, BudgetItemSection.SAVING, MoaZipPalette.Yellow50, onIntent)
                }
            }
        }
        Row(
            Modifier.fillMaxWidth().background(MoaZipPalette.Cream50).padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            MoaZipOutlinedButton(stringResource(R.string.budget_cancel), onBack, Modifier.weight(1f))
            MoaZipButton(
                text = if (state.isSaving) stringResource(R.string.budget_saving_in_progress) else stringResource(R.string.budget_save),
                onClick = onSave,
                modifier = Modifier.weight(1f),
                enabled = !state.isSaving,
            )
        }
    }
}

@Composable
private fun EditAllocationSection(
    title: String,
    description: String,
    items: List<BudgetAllocationUiModel>,
    memberIndex: Int?,
    section: BudgetItemSection,
    containerColor: androidx.compose.ui.graphics.Color,
    onIntent: (EditBudgetIntent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().background(containerColor, RoundedCornerShape(16.dp)).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(description, style = MaterialTheme.typography.bodySmall, color = MoaZipPalette.Gray500)
        items.forEachIndexed { itemIndex, item ->
            AllocationEditor(
                item = item,
                onNameChange = { onIntent(EditBudgetIntent.ChangeAllocationName(memberIndex, section, itemIndex, it)) },
                onAmountChange = { onIntent(EditBudgetIntent.ChangeAllocationAmount(memberIndex, section, itemIndex, it)) },
                onRemove = { onIntent(EditBudgetIntent.RemoveAllocation(memberIndex, section, itemIndex)) },
            )
        }
        TextButton(onClick = { onIntent(EditBudgetIntent.AddAllocation(memberIndex, section)) }) {
            Text(stringResource(if (section == BudgetItemSection.BUDGET) R.string.budget_add_allocation else R.string.budget_add_saving))
        }
    }
}

@Composable
private fun AllocationEditor(
    item: BudgetAllocationUiModel,
    onNameChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onRemove: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Text(stringResource(R.string.budget_item), fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            TextButton(onClick = onRemove) { Text(stringResource(R.string.budget_delete), color = MaterialTheme.colorScheme.error) }
        }
        MoaZipTextField(item.name, onNameChange, placeholder = stringResource(R.string.budget_item_placeholder))
        AmountField(item.amount, onAmountChange)
    }
}

@Composable
private fun AmountField(amount: Long, onValueChange: (String) -> Unit) {
    MoaZipTextField(
        value = if (amount == 0L) "" else NumberFormat.getNumberInstance(Locale.KOREA).format(amount),
        onValueChange = onValueChange,
        placeholder = stringResource(R.string.budget_amount_placeholder),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}

@Composable
private fun Long.asWon(): String = stringResource(R.string.budget_won_format, NumberFormat.getNumberInstance(Locale.KOREA).format(this))

private fun BudgetError.stringRes() = when (this) {
    BudgetError.UNAUTHENTICATED -> R.string.budget_error_unauthenticated
    BudgetError.LOAD_FAILED -> R.string.budget_error_load_failed
    BudgetError.SAVE_FAILED -> R.string.budget_error_save_failed
}
