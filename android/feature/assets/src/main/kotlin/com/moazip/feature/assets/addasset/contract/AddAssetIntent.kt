package com.moazip.feature.assets.addasset.contract

import com.moazip.core.presentation.mvi.UiIntent

sealed interface AddAssetIntent : UiIntent {
    data class NameChanged(val value: String) : AddAssetIntent
    data class OwnerSelected(val value: OwnerSelection) : AddAssetIntent
    data class AssetTypeSelected(val value: AssetType) : AddAssetIntent
    data class CategorySelected(val value: AssetCategory) : AddAssetIntent
    data class AmountChanged(val value: String) : AddAssetIntent
    data class MemoChanged(val value: String) : AddAssetIntent
    data object SaveClicked : AddAssetIntent
    data object CancelClicked : AddAssetIntent
}
