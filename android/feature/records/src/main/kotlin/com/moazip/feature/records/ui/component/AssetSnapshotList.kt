package com.moazip.feature.records.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipCard
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.records.R
import com.moazip.feature.records.contract.SnapshotRecordUiModel
import com.moazip.feature.records.ui.format.toKoreanAmount
import java.util.Locale

@Composable
internal fun AssetSnapshotList(records: List<SnapshotRecordUiModel>) {
    MoaZipCard {
        Column {
            Text(
                text = stringResource(R.string.asset_records_snapshot_list_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 6.dp),
            )
            records.forEachIndexed { index, record ->
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text = record.recordedDate,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MoaZipPalette.Gray900,
                    )
                    Text(
                        text = record.growthRate?.let { growthRate ->
                            val growthAmount = record.growthAmount ?: 0L
                            val signedAmount = buildString {
                                if (growthAmount > 0) append('+')
                                append(growthAmount.toKoreanAmount())
                            }
                            stringResource(
                                R.string.asset_records_growth_amount_rate,
                                signedAmount,
                                String.format(Locale.KOREA, "%+.1f", growthRate),
                            )
                        } ?: stringResource(R.string.asset_records_no_previous_record),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = when {
                            record.growthRate == null -> MoaZipPalette.Gray500
                            record.growthRate > 0 -> MoaZipPalette.Green600
                            record.growthRate < 0 -> MaterialTheme.colorScheme.error
                            else -> MoaZipPalette.Gray500
                        },
                    )
                    RecordDetailRow(
                        label = stringResource(R.string.asset_records_net_worth),
                        amount = record.netWorth,
                    )
                    RecordDetailRow(
                        label = stringResource(R.string.asset_records_financial_assets),
                        amount = record.financialAssetTotal,
                    )
                    RecordDetailRow(
                        label = stringResource(R.string.asset_records_deposit),
                        amount = record.depositTotal,
                    )
                    RecordDetailRow(
                        label = stringResource(R.string.asset_records_liability),
                        amount = -record.liabilityTotal,
                        isLiability = true,
                        highlightColor = when {
                            record.liabilityChange == null || record.liabilityChange == 0L -> null
                            record.liabilityChange > 0L -> MaterialTheme.colorScheme.errorContainer
                            else -> MoaZipPalette.Green600.copy(alpha = 0.14f)
                        },
                    )
                }
                if (index != records.lastIndex) {
                    HorizontalDivider(color = MoaZipPalette.Beige200)
                }
            }
        }
    }
}

@Composable
private fun RecordDetailRow(
    label: String,
    amount: Long,
    isLiability: Boolean = false,
    highlightColor: Color? = null,
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MoaZipPalette.Gray500,
        )
        Text(
            text = amount.toKoreanAmount(),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = if (isLiability) MaterialTheme.colorScheme.error else MoaZipPalette.Gray900,
            modifier = if (highlightColor != null) {
                Modifier
                    .background(highlightColor, RoundedCornerShape(4.dp))
                    .padding(horizontal = 5.dp, vertical = 2.dp)
            } else {
                Modifier
            },
        )
    }
}
