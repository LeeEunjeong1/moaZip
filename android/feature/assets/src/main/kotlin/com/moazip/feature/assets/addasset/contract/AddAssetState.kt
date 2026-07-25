package com.moazip.feature.assets.addasset.contract

import com.moazip.core.presentation.mvi.UiState

data class AddAssetState(
    val name: String = "",
    val memberNames: List<String> = emptyList(),
    val owner: OwnerSelection = OwnerSelection.Common,
    val assetType: AssetType = AssetType.ASSET,
    val category: AssetCategory? = null,
    val amount: String = "",
    val memo: String = "",
) : UiState {
    val availableCategories: List<AssetCategory>
        get() = when (assetType) {
            AssetType.ASSET -> listOf(
                AssetCategory.LEASE_DEPOSIT,
                AssetCategory.DEPOSIT,
                AssetCategory.SAVINGS,
                AssetCategory.CHECKING,
                AssetCategory.RETIREMENT,
                AssetCategory.HOUSING_SUBSCRIPTION,
                AssetCategory.OTHER,
            )
            AssetType.INVESTMENT -> listOf(
                AssetCategory.ISA,
                AssetCategory.OVERSEAS_STOCK,
                AssetCategory.DOMESTIC_STOCK,
                AssetCategory.OTHER,
            )
            AssetType.DEBT -> listOf(
                AssetCategory.LOAN,
                AssetCategory.OTHER,
            )
        }
}
