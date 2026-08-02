package com.moazip.feature.assets.assetlist.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.assets.R
import java.text.NumberFormat
import java.util.Locale

@Composable
internal fun AssetSummaryCard(
    netWorth: Long,
    assetTotal: Long,
    investmentTotal: Long,
    liabilityTotal: Long,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MoaZipPalette.Yellow50, RoundedCornerShape(20.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(R.string.asset_list_net_worth),
                color = MoaZipPalette.Gray500,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(
                    R.string.asset_list_amount_won,
                    netWorth.toFormattedAmount(),
                ),
                color = MoaZipPalette.Gray900,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SummaryAmount(
                label = stringResource(R.string.asset_list_asset),
                amount = assetTotal,
            )
            SummaryAmount(
                label = stringResource(R.string.asset_list_investment),
                amount = investmentTotal,
            )
            SummaryAmount(
                label = stringResource(R.string.asset_list_liability),
                amount = liabilityTotal,
            )
        }
    }
}

@Composable
private fun SummaryAmount(
    label: String,
    amount: Long,
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            color = MoaZipPalette.Gray500,
            style = MaterialTheme.typography.labelMedium,
        )
        Text(
            text = stringResource(R.string.asset_list_amount_won, amount.toFormattedAmount()),
            color = MoaZipPalette.Gray900,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

internal fun Long.toFormattedAmount(): String =
    NumberFormat.getNumberInstance(Locale.KOREA).format(this)
