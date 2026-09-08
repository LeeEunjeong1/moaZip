package com.moazip.feature.budget.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipCard
import com.moazip.feature.budget.contract.BudgetAllocationUiModel

@Composable
internal fun JointSavingCard(title: String, items: List<BudgetAllocationUiModel>) {
    MoaZipCard {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            items.forEach { BudgetRow(it) }
        }
    }
}
