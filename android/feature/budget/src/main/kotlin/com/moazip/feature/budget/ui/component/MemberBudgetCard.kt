package com.moazip.feature.budget.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipCard
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.budget.contract.BudgetAllocationUiModel
import com.moazip.feature.budget.contract.MemberBudgetUiModel

@Composable
internal fun MemberBudgetCard(member: MemberBudgetUiModel) {
    MoaZipCard {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(member.name, style = MaterialTheme.typography.titleLarge)
                Text("월급", style = MaterialTheme.typography.labelMedium, color = MoaZipPalette.Gray500)
            }
            Text(member.income.toWon(), style = MaterialTheme.typography.titleMedium)
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = MoaZipPalette.Beige200)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            member.allocations.forEach { BudgetRow(it) }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = MoaZipPalette.Beige200)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("배분 후 잔액", color = MoaZipPalette.Gray500)
            Text(member.remainingAmount.toWon(), fontWeight = FontWeight.Bold, color = MoaZipPalette.Green600)
        }
    }
}

@Composable
internal fun BudgetRow(item: BudgetAllocationUiModel) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(item.name, style = MaterialTheme.typography.bodyMedium, color = MoaZipPalette.Gray500)
        Text(item.amount.toWon(), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}
