package com.moazip.feature.budget.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipButton
import com.moazip.core.ui.component.MoaZipOutlinedButton
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.budget.contract.MonthlyBudgetState
import com.moazip.feature.budget.contract.MemberBudgetUiModel
import com.moazip.feature.budget.R
import com.moazip.feature.budget.contract.BudgetError
import com.moazip.feature.budget.ui.component.BudgetSummaryCard
import com.moazip.feature.budget.ui.component.JointSavingCard
import com.moazip.feature.budget.ui.component.MemberBudgetCard

@Composable
fun MonthlyBudgetScreen(
    state: MonthlyBudgetState,
    onEditClick: () -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().background(MoaZipPalette.Cream50).padding(horizontal = 24.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Text(stringResource(R.string.budget_title), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(
                stringResource(R.string.budget_description, formatBudgetMonth(state.monthId)),
                style = MaterialTheme.typography.bodyMedium,
                color = MoaZipPalette.Gray500,
            )
        }
        item { BudgetSummaryCard(state) }
        if (state.isCopiedFromPreviousMonth) {
            item {
                Text(
                    text = stringResource(R.string.budget_copied_from_previous),
                    color = MoaZipPalette.Gray500,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        state.error?.let { error ->
            item { Text(stringResource(error.stringRes()), color = MaterialTheme.colorScheme.error) }
        }
        items(state.members, key = MemberBudgetUiModel::name) { MemberBudgetCard(it) }
        if (state.jointAllocations.isNotEmpty()) item { JointSavingCard(stringResource(R.string.budget_joint_budget), state.jointAllocations) }
        if (state.jointSavings.isNotEmpty()) item { JointSavingCard(stringResource(R.string.budget_joint_saving), state.jointSavings) }
        item { MoaZipButton(text = stringResource(R.string.budget_edit_plan), onClick = onEditClick) }
        item { MoaZipOutlinedButton(text = stringResource(R.string.budget_view_history), onClick = onHistoryClick) }
    }
}

private fun BudgetError.stringRes() = when (this) {
    BudgetError.UNAUTHENTICATED -> R.string.budget_error_unauthenticated
    BudgetError.LOAD_FAILED -> R.string.budget_error_load_failed
    BudgetError.SAVE_FAILED -> R.string.budget_error_save_failed
}
