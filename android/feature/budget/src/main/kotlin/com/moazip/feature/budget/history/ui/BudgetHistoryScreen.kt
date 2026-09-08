package com.moazip.feature.budget.history.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipCard
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.budget.R
import com.moazip.feature.budget.contract.BudgetError
import com.moazip.feature.budget.contract.MonthlyBudgetState
import com.moazip.feature.budget.history.contract.BudgetHistoryState
import com.moazip.feature.budget.ui.formatBudgetMonth
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BudgetHistoryScreen(state: BudgetHistoryState, onBack: () -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize().background(MoaZipPalette.Cream50).padding(horizontal = 24.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            TextButton(onClick = onBack, contentPadding = PaddingValues(0.dp)) { Text(stringResource(R.string.budget_edit_back)) }
            Text(stringResource(R.string.budget_history_title), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(stringResource(R.string.budget_history_description), color = MoaZipPalette.Gray500)
        }
        state.error?.let { error -> item { Text(stringResource(error.stringRes()), color = MaterialTheme.colorScheme.error) } }
        if (!state.isLoading && state.plans.isEmpty() && state.error == null) {
            item { Text(stringResource(R.string.budget_history_empty), color = MoaZipPalette.Gray500) }
        }
        items(state.plans, key = MonthlyBudgetState::monthId) { BudgetHistoryCard(it) }
    }
}

@Composable
private fun BudgetHistoryCard(plan: MonthlyBudgetState) {
    MoaZipCard {
        Text(formatBudgetMonth(plan.monthId), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(
            stringResource(R.string.budget_history_income, plan.totalIncome.toWon()),
            modifier = Modifier.padding(top = 4.dp, bottom = 14.dp),
            color = MoaZipPalette.Gray500,
        )
        HistoryRow(stringResource(R.string.budget_history_spending), plan.totalAllocation)
        HistoryRow(stringResource(R.string.budget_history_planned_saving), plan.plannedSaving)
        HistoryRow(stringResource(R.string.budget_history_remaining), plan.remainingSaving, MoaZipPalette.Green600)
    }
}

@Composable
private fun HistoryRow(label: String, amount: Long, color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MoaZipPalette.Gray500)
        Text(amount.toWon(), color = color, fontWeight = FontWeight.SemiBold)
    }
}

private fun Long.toWon() = NumberFormat.getCurrencyInstance(Locale.KOREA).format(this)

private fun BudgetError.stringRes() = when (this) {
    BudgetError.UNAUTHENTICATED -> R.string.budget_error_unauthenticated
    BudgetError.LOAD_FAILED -> R.string.budget_error_load_failed
    BudgetError.SAVE_FAILED -> R.string.budget_error_save_failed
}
