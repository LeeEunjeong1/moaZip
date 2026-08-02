package com.moazip.feature.assets.assetlist.contract

sealed interface AssetListIntent {
    data class FilterSelected(val filter: AssetListFilter) : AssetListIntent
    data class AssetClicked(val assetId: String) : AssetListIntent
}
