package com.moazip.feature.records.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.moazip.core.ui.component.MoaZipCard
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.records.ui.format.toKoreanAmount
import com.moazip.feature.records.R

@Composable
internal fun QuarterSummaryCard(
    quarter: String,
    netWorth: Long,
    difference: Long,
) {
    MoaZipCard {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = quarter,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MoaZipPalette.Gray500,
            )
            Text(
                text = netWorth.toKoreanAmount(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MoaZipPalette.Gray900,
            )
            Text(
                text = stringResource(
                    R.string.asset_records_difference,
                    "${if (difference >= 0) "+" else ""}${difference.toKoreanAmount()}",
                ),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = if (difference >= 0) MoaZipPalette.Green600 else MaterialTheme.colorScheme.error,
            )
        }
    }
}
