package com.moazip.feature.dashboard.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipCard
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.dashboard.R
import com.moazip.feature.dashboard.contract.DashboardAssetTrendPoint
import java.text.NumberFormat
import java.util.Locale

@Composable
internal fun AssetTrendCard(points: List<DashboardAssetTrendPoint>) {
    if (points.isEmpty()) return
    val forecast = points.lastOrNull { it.isForecast }
    MoaZipCard {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = stringResource(R.string.dashboard_asset_trend_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            forecast?.let {
                Text(
                    text = stringResource(
                        R.string.dashboard_asset_trend_forecast,
                        it.netWorth.toWon(),
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MoaZipPalette.Gray500,
                )
            }
            AssetTrendChart(points = points)
            Row(modifier = Modifier.fillMaxWidth()) {
                points.forEach { point ->
                    Text(
                        text = point.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (point.isForecast) MoaZipPalette.Yellow500 else MoaZipPalette.Gray500,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            if (forecast != null) {
                Text(
                    text = stringResource(R.string.dashboard_asset_trend_notice),
                    style = MaterialTheme.typography.labelSmall,
                    color = MoaZipPalette.Gray500,
                )
            }
        }
    }
}

@Composable
private fun AssetTrendChart(points: List<DashboardAssetTrendPoint>) {
    val lineColor = MoaZipPalette.Yellow500
    val gridColor = MoaZipPalette.Beige200
    Canvas(modifier = Modifier.fillMaxWidth().height(150.dp)) {
        val values = points.map(DashboardAssetTrendPoint::netWorth)
        val minValue = values.minOrNull() ?: 0L
        val maxValue = values.maxOrNull() ?: 1L
        val range = (maxValue - minValue).coerceAtLeast(1L)
        val topPadding = 12.dp.toPx()
        val bottomPadding = 12.dp.toPx()
        val chartHeight = size.height - topPadding - bottomPadding
        val stepX = if (points.size <= 1) 0f else size.width / (points.size - 1)
        fun offset(index: Int): Offset {
            val normalized = (points[index].netWorth - minValue).toFloat() / range.toFloat()
            return Offset(index * stepX, topPadding + chartHeight * (1f - normalized))
        }

        repeat(3) { index ->
            val y = topPadding + chartHeight * index / 2f
            drawLine(gridColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 1.dp.toPx())
        }

        val forecastIndex = points.indexOfFirst { it.isForecast }
        val actualEndExclusive = if (forecastIndex >= 0) forecastIndex else points.size
        if (actualEndExclusive >= 2) {
            val actualPath = Path().apply {
                moveTo(offset(0).x, offset(0).y)
                for (index in 1 until actualEndExclusive) {
                    lineTo(offset(index).x, offset(index).y)
                }
            }
            drawPath(actualPath, lineColor, style = Stroke(width = 3.dp.toPx()))
        }
        if (forecastIndex > 0) {
            drawLine(
                color = lineColor,
                start = offset(forecastIndex - 1),
                end = offset(forecastIndex),
                strokeWidth = 3.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10.dp.toPx(), 7.dp.toPx())),
            )
        }
        points.forEachIndexed { index, point ->
            drawCircle(
                color = if (point.isForecast) MoaZipPalette.Cream50 else lineColor,
                radius = 5.dp.toPx(),
                center = offset(index),
            )
            if (point.isForecast) {
                drawCircle(
                    color = lineColor,
                    radius = 5.dp.toPx(),
                    center = offset(index),
                    style = Stroke(width = 2.dp.toPx()),
                )
            }
        }
    }
}

private fun Long.toWon(): String = NumberFormat.getCurrencyInstance(Locale.KOREA).format(this)
