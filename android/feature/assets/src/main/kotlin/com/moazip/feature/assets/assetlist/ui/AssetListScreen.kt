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
import com.moazip.feature.assets.assetlist.ui.component.AssetSummaryCard

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
        if (state.filteredAssets.isEmpty()) {
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

@Preview(showBackground = true, widthDp = 393, heightDp = 780)
@Composable
private fun AssetListScreenPreview() {
    MoaZipTheme {
        AssetListScreen(
            state = AssetListState(
                assets = listOf(
                    AssetListItemUiModel(
                        id = "1",
                        name = "신한은행 입출금",
                        categoryName = "입출금",
                        ownerName = "공동",
                        amount = 12_500_000,
                        kind = AssetListFilter.ASSET,
                    ),
                    AssetListItemUiModel(
                        id = "2",
                        name = "ISA 계좌",
                        categoryName = "ISA",
                        ownerName = "최재웅",
                        amount = 8_300_000,
                        kind = AssetListFilter.INVESTMENT,
                    ),
                    AssetListItemUiModel(
                        id = "3",
                        name = "전세 대출",
                        categoryName = "대출",
                        ownerName = "공동",
                        amount = 50_000_000,
                        kind = AssetListFilter.LIABILITY,
                    ),
                ),
            ),
            onIntent = {},
        )
    }
}
