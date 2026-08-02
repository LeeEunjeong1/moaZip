package com.moazip.feature.assets.assetlist.contract

data class AssetListState(
    val selectedFilter: AssetListFilter = AssetListFilter.ALL,
    val assets: List<AssetListItemUiModel> = emptyList(),
    val isLoading: Boolean = false,
) {
    val assetTotal: Long
        get() = assets
            .filter { it.kind == AssetListFilter.ASSET }
            .sumOf(AssetListItemUiModel::amount)

    val investmentTotal: Long
        get() = assets
            .filter { it.kind == AssetListFilter.INVESTMENT }
            .sumOf(AssetListItemUiModel::amount)

    val liabilityTotal: Long
        get() = assets
            .filter { it.kind == AssetListFilter.LIABILITY }
            .sumOf(AssetListItemUiModel::amount)

    val netWorth: Long
        get() = assetTotal + investmentTotal - liabilityTotal

    val filteredAssets: List<AssetListItemUiModel>
        get() = if (selectedFilter == AssetListFilter.ALL) {
            assets
        } else {
            assets.filter { it.kind == selectedFilter }
        }
}
