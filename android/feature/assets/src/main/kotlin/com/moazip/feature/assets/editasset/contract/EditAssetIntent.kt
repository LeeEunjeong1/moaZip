package com.moazip.feature.assets.editasset.contract

import com.moazip.core.presentation.mvi.UiIntent
import com.moazip.feature.assets.addasset.contract.AssetCategory
import com.moazip.feature.assets.addasset.contract.AssetType
import com.moazip.feature.assets.addasset.contract.OwnerSelection

sealed interface EditAssetIntent : UiIntent {
    data class NameChanged(val value: String) : EditAssetIntent
    data class OwnerSelected(val value: OwnerSelection) : EditAssetIntent
    data class AssetTypeSelected(val value: AssetType) : EditAssetIntent
    data class CategorySelected(val value: AssetCategory) : EditAssetIntent
    data class AmountChanged(val value: String) : EditAssetIntent
    data class MemoChanged(val value: String) : EditAssetIntent
    data object SaveClicked : EditAssetIntent
    data object DeleteClicked : EditAssetIntent
    data object DeleteDismissed : EditAssetIntent
    data object DeleteConfirmed : EditAssetIntent
    data object CancelClicked : EditAssetIntent
}
