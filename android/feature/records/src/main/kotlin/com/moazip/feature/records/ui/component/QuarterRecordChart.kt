package com.moazip.feature.records.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.records.contract.QuarterRecordUiModel
import java.util.Locale

@Composable
internal fun QuarterRecordChart(records: List<QuarterRecordUiModel>) {
    val maxValue = records.maxOfOrNull { it.netWorth }?.coerceAtLeast(1L) ?: 1L
    Row(modifier = Modifier.fillMaxWidth().height(132.dp)) {
        records.forEachIndexed { index, record ->
            val barHeight = (22f + 52f * record.netWorth.coerceAtLeast(0L) / maxValue).dp
            val selected = index == records.lastIndex
            Column(
                modifier = Modifier.weight(1f).height(132.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(1f))
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
