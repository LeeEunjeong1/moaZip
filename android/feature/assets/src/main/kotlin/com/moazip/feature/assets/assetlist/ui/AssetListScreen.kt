package com.moazip.feature.assets.assetlist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
}
