package com.moazip.feature.budget.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipCard
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.budget.contract.BudgetAllocationUiModel
import com.moazip.feature.budget.contract.MemberBudgetUiModel
import com.moazip.feature.budget.R

@Composable
internal fun MemberBudgetCard(member: MemberBudgetUiModel) {
    MoaZipCard {
        Text(member.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(stringResource(R.string.budget_salary), style = MaterialTheme.typography.bodyMedium, color = MoaZipPalette.Gray500)
            Text(member.income.toWon(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = MoaZipPalette.Beige200)
        BudgetSection(
            title = stringResource(R.string.budget_allocation_title),
            description = stringResource(R.string.budget_allocation_description),
            items = member.budgetAllocations,
            containerColor = MoaZipPalette.Gray50,
        )
        BudgetSection(
            title = stringResource(R.string.budget_saving_plan_title),
            description = stringResource(R.string.budget_saving_plan_description),
            items = member.savings,
            containerColor = MoaZipPalette.Yellow50,
            modifier = Modifier.padding(top = 12.dp),
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = MoaZipPalette.Beige200)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(stringResource(R.string.budget_remaining_saving), color = MoaZipPalette.Gray500)
            Text(member.remainingAmount.toWon(), fontWeight = FontWeight.Bold, color = MoaZipPalette.Green600)
        }
    }
}

@Composable
private fun BudgetSection(
    title: String,
    description: String,
    items: List<BudgetAllocationUiModel>,
    containerColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth().background(containerColor, RoundedCornerShape(14.dp)).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(9.dp),
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(description, style = MaterialTheme.typography.labelSmall, color = MoaZipPalette.Gray500)
            }
            Text(items.sumOf(BudgetAllocationUiModel::amount).toWon(), fontWeight = FontWeight.Bold)
        }
        items.forEach { BudgetRow(it) }
        if (items.isEmpty()) Text(stringResource(R.string.budget_empty_items), style = MaterialTheme.typography.bodySmall, color = MoaZipPalette.Gray500)
    }
}

@Composable
internal fun BudgetRow(item: BudgetAllocationUiModel) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(item.name, style = MaterialTheme.typography.bodyMedium, color = MoaZipPalette.Gray500)
        Text(item.amount.toWon(), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}
