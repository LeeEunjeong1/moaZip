package com.moazip.feature.assets.assetlist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.core.ui.theme.MoaZipTheme
import com.moazip.feature.assets.assetlist.contract.AssetListFilter
import com.moazip.feature.assets.assetlist.contract.AssetListIntent
import com.moazip.feature.assets.assetlist.contract.AssetListItemUiModel
import com.moazip.feature.assets.assetlist.contract.AssetListState
import com.moazip.feature.assets.assetlist.ui.component.AssetFilterRow
import com.moazip.feature.assets.assetlist.ui.component.AssetListEmpty
import com.moazip.feature.assets.assetlist.ui.component.AssetListHeader
import com.moazip.feature.assets.assetlist.ui.component.AssetListItem
import com.moazip.feature.assets.assetlist.ui.component.AssetListToolbar
import com.moazip.feature.assets.assetlist.ui.component.AssetListLoading
import com.moazip.feature.assets.assetlist.ui.component.AssetListErrorContent
import com.moazip.feature.assets.assetlist.ui.component.AssetOwnerFilterRow
import com.moazip.feature.assets.assetlist.ui.component.AssetSummaryCard
import com.moazip.feature.assets.assetlist.ui.component.AssetSnapshotRecordCard
import com.moazip.feature.assets.R
import com.moazip.core.model.AssetCategory

@Composable
fun AssetListScreen(
    state: AssetListState,
    onIntent: (AssetListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MoaZipPalette.Cream50)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Column(modifier = Modifier.padding(top = 20.dp)) {
                AssetListHeader()
            }
        }
        item {
            AssetSummaryCard(
                netWorth = state.netWorth,
                assetTotal = state.assetTotal,
                investmentTotal = state.investmentTotal,
                liabilityTotal = state.liabilityTotal,
                recordedAtMillis = state.recordedAtMillis,
            )
        }
        item {
            AssetSnapshotRecordCard(
                isRecorded = state.isCurrentMonthRecorded,
                isRecording = state.isRecordingSnapshot,
                showError = state.snapshotRecordFailed,
                onRecordClick = { onIntent(AssetListIntent.RecordSnapshotClicked) },
            )
        }
        item {
            AssetFilterRow(
                selectedFilter = state.selectedFilter,
                onFilterSelected = { filter ->
                    onIntent(AssetListIntent.FilterSelected(filter))
                },
            )
        }
        item {
            AssetOwnerFilterRow(
                filters = state.ownerFilters,
                selectedFilter = state.selectedOwnerFilter,
                onFilterSelected = { filter ->
                    onIntent(AssetListIntent.OwnerFilterSelected(filter))
                },
            )
        }
        item {
            AssetListToolbar(
                assetCount = state.filteredAssets.size,
                selectedSortOption = state.sortOption,
                onSortOptionSelected = { option ->
                    onIntent(AssetListIntent.SortOptionSelected(option))
                },
            )
        }
        if (state.isLoading) {
            item { AssetListLoading() }
        } else if (state.error != null) {
            item {
                AssetListErrorContent(
                    error = state.error,
                    onRetry = { onIntent(AssetListIntent.RetryClicked) },
                )
            }
        } else if (state.filteredAssets.isEmpty()) {
            item {
                AssetListEmpty(
                    modifier = Modifier.padding(bottom = 28.dp),
                )
            }
        } else {
            items(
                items = state.filteredAssets,
                key = AssetListItemUiModel::id,
            ) { asset ->
                AssetListItem(
                    asset = asset,
                    onClick = {
                        onIntent(AssetListIntent.AssetClicked(asset.id))
                    },
                )
            }
            item {
                Column(modifier = Modifier.padding(bottom = 16.dp)) {}
            }
        }
    }
    if (state.showRecordConfirmation) {
        AlertDialog(
            onDismissRequest = { onIntent(AssetListIntent.RecordSnapshotDismissed) },
            containerColor = MoaZipPalette.White,
            title = { Text(stringResource(R.string.asset_snapshot_dialog_title)) },
            text = {
                Text(
                    stringResource(
                        if (state.isCurrentMonthRecorded) R.string.asset_snapshot_dialog_overwrite
                        else R.string.asset_snapshot_dialog_description,
                    ),
                )
            },
            confirmButton = {
                TextButton(onClick = { onIntent(AssetListIntent.RecordSnapshotConfirmed) }) {
                    Text(stringResource(R.string.asset_snapshot_dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { onIntent(AssetListIntent.RecordSnapshotDismissed) }) {
                    Text(stringResource(R.string.asset_snapshot_dialog_cancel))
                }
            },
        )
    }
}
