package com.moazip.feature.budget.history.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.moazip.feature.budget.history.BudgetHistoryViewModel
import com.moazip.feature.budget.history.ui.BudgetHistoryScreen

@Composable
fun BudgetHistoryRoute(viewModel: BudgetHistoryViewModel, onBack: () -> Unit, modifier: Modifier = Modifier) {
    val state by viewModel.state.collectAsState()
    BudgetHistoryScreen(state = state, onBack = onBack, modifier = modifier)
}
