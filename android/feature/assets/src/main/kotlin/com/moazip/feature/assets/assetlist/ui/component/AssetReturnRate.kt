package com.moazip.feature.assets.assetlist.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.assets.R
import java.text.NumberFormat

@Composable
internal fun AssetReturnRate(
    profit: Long?,
    returnRate: Double,
) {
    val percentage = NumberFormat.getNumberInstance().apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }.format(returnRate * 100)
    val sign = if (returnRate > 0) "+" else ""
    val text = if (profit == null) {
        stringResource(R.string.asset_list_return_rate, sign, percentage)
    } else {
        stringResource(
            R.string.asset_list_profit_and_return_rate,
            if (profit > 0) "+" else "",
            profit.toFormattedAmount(),
            sign,
            percentage,
        )
    }

    Text(
        text = text,
        color = returnRate.returnRateColor(),
        style = MaterialTheme.typography.labelSmall,
    )
}

private fun Double.returnRateColor(): Color = when {
    this > 0 -> MoaZipPalette.Green600
    else -> MoaZipPalette.Gray500
}
