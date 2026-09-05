package com.moazip.feature.budget.editbudget.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.moazip.feature.budget.editbudget.EditBudgetViewModel
import com.moazip.feature.budget.editbudget.contract.EditBudgetIntent
import com.moazip.feature.budget.editbudget.ui.EditBudgetScreen

@Composable
fun EditBudgetRoute(viewModel: EditBudgetViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.state.collectAsState()
    EditBudgetScreen(
        state = state,
        onIntent = viewModel::onIntent,
        onSave = { viewModel.onIntent(EditBudgetIntent.SaveClicked) },
        onBack = { viewModel.onIntent(EditBudgetIntent.CancelClicked) },
        modifier = modifier,
    )
}
