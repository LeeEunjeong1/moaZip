package com.moazip.feature.assets.assetlist.contract

import com.moazip.core.presentation.mvi.UiIntent

sealed interface AssetListIntent : UiIntent {
    data class FilterSelected(val filter: AssetListFilter) : AssetListIntent
    data class AssetClicked(val assetId: String) : AssetListIntent
    data object RetryClicked : AssetListIntent
}
