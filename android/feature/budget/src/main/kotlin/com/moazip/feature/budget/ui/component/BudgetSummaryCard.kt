package com.moazip.feature.budget.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipCard
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.budget.contract.MonthlyBudgetState
import java.text.NumberFormat
import java.util.Locale

@Composable
internal fun BudgetSummaryCard(state: MonthlyBudgetState) {
    MoaZipCard {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("이번 달 예상 저축", style = MaterialTheme.typography.titleMedium)
            Text(
                text = state.availableSaving.toWon(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = if (state.availableSaving >= 0) MoaZipPalette.Green600 else MaterialTheme.colorScheme.error,
            )
            Text(
                text = "월급의 ${String.format(Locale.KOREA, "%.1f", state.savingRate)}%를 더 모을 수 있어요",
                style = MaterialTheme.typography.bodyMedium,
                color = MoaZipPalette.Gray500,
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                SummaryValue("총수입", state.totalIncome)
                SummaryValue("계획 배분", state.totalAllocation)
            }
        }
    }
}

@Composable
private fun SummaryValue(label: String, amount: Long) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MoaZipPalette.Gray500)
        Text(amount.toWon(), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}

internal fun Long.toWon(): String = NumberFormat.getCurrencyInstance(Locale.KOREA).format(this)
