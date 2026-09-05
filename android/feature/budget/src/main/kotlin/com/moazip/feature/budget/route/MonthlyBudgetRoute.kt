package com.moazip.feature.budget.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.moazip.feature.budget.MonthlyBudgetViewModel
import com.moazip.feature.budget.ui.MonthlyBudgetScreen

@Composable
fun MonthlyBudgetRoute(
    viewModel: MonthlyBudgetViewModel,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    MonthlyBudgetScreen(state = state, onEditClick = onEditClick, modifier = modifier)
}
