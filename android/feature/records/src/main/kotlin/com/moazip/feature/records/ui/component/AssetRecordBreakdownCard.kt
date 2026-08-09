package com.moazip.feature.records.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipCard
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.records.R
import com.moazip.feature.records.ui.format.toKoreanAmount

@Composable
internal fun AssetRecordBreakdownCard(
    assetTotal: Long,
    depositTotal: Long,
    liabilityTotal: Long,
) {
    MoaZipCard {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            BreakdownRow(stringResource(R.string.asset_records_financial_assets), assetTotal)
            BreakdownRow(stringResource(R.string.asset_records_deposit), depositTotal)
            BreakdownRow(
                label = stringResource(R.string.asset_records_liability),
                amount = -liabilityTotal,
                isLiability = true,
            )
        }
    }
}

@Composable
private fun BreakdownRow(label: String, amount: Long, isLiability: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MoaZipPalette.Gray500, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = amount.toKoreanAmount(),
            color = if (isLiability) MaterialTheme.colorScheme.error else MoaZipPalette.Gray900,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
