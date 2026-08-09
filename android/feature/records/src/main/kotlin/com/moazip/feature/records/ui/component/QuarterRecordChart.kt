package com.moazip.feature.records.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.records.contract.QuarterRecordUiModel
import java.text.NumberFormat
import java.util.Locale

@Composable
internal fun QuarterRecordChart(records: List<QuarterRecordUiModel>) {
    val maxValue = records.maxOfOrNull { it.netWorth }?.coerceAtLeast(1L) ?: 1L
    val scrollState = rememberScrollState()

    LaunchedEffect(records.size, scrollState.maxValue) {
        if (scrollState.maxValue > 0) scrollState.scrollTo(scrollState.maxValue)
    }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val itemWidth = maxWidth / VISIBLE_RECORD_COUNT
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(CHART_HEIGHT)
                .horizontalScroll(scrollState),
        ) {
            records.forEachIndexed { index, record ->
                val barHeight = (22f + 52f * record.netWorth.coerceAtLeast(0L) / maxValue).dp
                val selected = index == records.lastIndex
                Column(
                    modifier = Modifier.width(itemWidth).height(CHART_HEIGHT),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = record.netWorth.toCompactAmount(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    record.growthRate?.let { growthRate ->
                        Text(
                            text = String.format(Locale.KOREA, "%+.1f%%", growthRate),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = when {
                                growthRate > 0 -> MoaZipPalette.Green600
                                growthRate < 0 -> MaterialTheme.colorScheme.error
                                else -> MoaZipPalette.Gray500
                            },
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    Box(
                        modifier = Modifier
                            .width(8.dp)
                            .height(barHeight)
                            .background(
                                color = if (selected) MoaZipPalette.Yellow500 else MoaZipPalette.Beige200,
                                shape = RoundedCornerShape(4.dp),
                            ),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = record.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MoaZipPalette.Gray500,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

private const val VISIBLE_RECORD_COUNT = 5
private val CHART_HEIGHT = 152.dp

private fun Long.toCompactAmount(): String {
    val absolute = kotlin.math.abs(this)
    val sign = if (this < 0) "-" else ""
    return when {
        absolute >= 100_000_000L -> {
            val eok = absolute / 100_000_000.0
            val amount = if (absolute % 100_000_000L == 0L) {
                eok.toLong().toString()
            } else {
                String.format(Locale.KOREA, "%.1f", eok)
            }
            "$sign${amount}억"
        }
        absolute >= 10_000L -> "$sign${NumberFormat.getNumberInstance(Locale.KOREA).format(absolute / 10_000L)}만"
        else -> "$sign${NumberFormat.getNumberInstance(Locale.KOREA).format(absolute)}"
    }
}
