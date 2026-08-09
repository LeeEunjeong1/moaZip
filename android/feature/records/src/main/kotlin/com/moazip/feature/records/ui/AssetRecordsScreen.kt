package com.moazip.feature.records.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.core.ui.theme.MoaZipTheme
import com.moazip.feature.records.contract.AssetRecordsState
import com.moazip.feature.records.contract.AssetRecordsIntent
import com.moazip.feature.records.ui.component.AssetSnapshotList
import com.moazip.feature.records.ui.component.AssetRecordsHeader
import com.moazip.feature.records.ui.component.AssetRecordsLoading
import com.moazip.feature.records.ui.component.AssetRecordsEmpty
import com.moazip.feature.records.ui.component.AssetRecordsErrorContent
import com.moazip.feature.records.ui.component.QuarterRecordChart
import com.moazip.feature.records.ui.component.QuarterSummaryCard

@Composable
fun AssetRecordsScreen(
    state: AssetRecordsState,
    onIntent: (AssetRecordsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MoaZipPalette.Cream50)
            .padding(horizontal = 24.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item { AssetRecordsHeader() }
        when {
            state.isLoading -> item { AssetRecordsLoading() }
            state.error != null -> item {
                AssetRecordsErrorContent(
                    error = state.error,
                    onRetry = { onIntent(AssetRecordsIntent.RetryClicked) },
                )
            }
            state.quarterRecords.isEmpty() -> item { AssetRecordsEmpty() }
            else -> {
                item {
                    QuarterSummaryCard(
                        quarter = state.selectedQuarter,
                        netWorth = state.netWorth,
                        growthRate = state.previousRecordGrowthRate,
                    )
                }
                item { QuarterRecordChart(records = state.quarterRecords) }
                item { AssetSnapshotList(records = state.snapshotRecords) }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 760)
@Composable
private fun AssetRecordsScreenPreview() {
    MoaZipTheme {
        AssetRecordsScreen(
            state = AssetRecordsState(
                selectedQuarter = "2026년 3분기",
                netWorth = 124_500_000,
                previousRecordGrowthRate = 7.3,
                quarterRecords = listOf(
                    com.moazip.feature.records.contract.QuarterRecordUiModel("25.09\n30", 62_000_000),
                    com.moazip.feature.records.contract.QuarterRecordUiModel("25.12\n31", 78_000_000),
                    com.moazip.feature.records.contract.QuarterRecordUiModel("26.03\n31", 92_000_000),
                    com.moazip.feature.records.contract.QuarterRecordUiModel("26.06\n30", 116_000_000),
                    com.moazip.feature.records.contract.QuarterRecordUiModel("26.08\n09", 124_500_000),
                ),
                snapshotRecords = listOf(
                    com.moazip.feature.records.contract.SnapshotRecordUiModel("3", "2026년 8월 9일", 124_500_000, 98_000_000, 87_000_000, 62_500_000),
                    com.moazip.feature.records.contract.SnapshotRecordUiModel("2", "2026년 6월 30일", 116_000_000, 91_000_000, 82_000_000, 57_000_000),
                    com.moazip.feature.records.contract.SnapshotRecordUiModel("1", "2026년 3월 31일", 92_000_000, 76_000_000, 70_000_000, 54_000_000),
                ),
                isLoading = false,
            ),
            onIntent = {},
        )
    }
}
