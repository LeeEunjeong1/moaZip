package com.moazip.feature.records.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.core.ui.theme.MoaZipTheme
import com.moazip.feature.records.R
import com.moazip.feature.records.contract.AssetRecordsState
import com.moazip.feature.records.ui.component.AssetRecordBreakdownCard
import com.moazip.feature.records.ui.component.AssetRecordsHeader
import com.moazip.feature.records.ui.component.QuarterRecordChart
import com.moazip.feature.records.ui.component.QuarterSummaryCard

@Composable
fun AssetRecordsScreen(
    state: AssetRecordsState,
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
        item {
            QuarterSummaryCard(
                quarter = state.selectedQuarter,
                netWorth = state.netWorth,
                difference = state.previousQuarterDifference,
            )
        }
        item { QuarterRecordChart(records = state.quarterRecords) }
        item {
            AssetRecordBreakdownCard(
                assetTotal = state.assetTotal,
                depositTotal = state.depositTotal,
                liabilityTotal = state.liabilityTotal,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 760)
@Composable
private fun AssetRecordsScreenPreview() {
    MoaZipTheme {
        AssetRecordsScreen(state = AssetRecordsState())
    }
}
