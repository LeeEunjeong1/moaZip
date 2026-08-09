package com.moazip.feature.dashboard.ui.component

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
import com.moazip.core.model.AssetSnapshot
import com.moazip.core.ui.component.MoaZipCard
import com.moazip.feature.dashboard.R
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun LatestAssetRecordCard(
    snapshot: AssetSnapshot?,
    modifier: Modifier = Modifier,
) {
    MoaZipCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = stringResource(R.string.dashboard_latest_record_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            if (snapshot == null) {
                Text(
                    text = stringResource(R.string.dashboard_latest_record_empty),
                    style = MaterialTheme.typography.bodyMedium,
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(snapshot.recordedAtMillis.asRecordedDate())
                    Text(
                        text = snapshot.netWorth.asWon(),
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

private fun Long.asWon(): String = NumberFormat.getCurrencyInstance(Locale.KOREA).format(this)

private fun Long.asRecordedDate(): String = Instant.ofEpochMilli(this)
    .atZone(ZoneId.systemDefault())
    .format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 기록", Locale.KOREA))
