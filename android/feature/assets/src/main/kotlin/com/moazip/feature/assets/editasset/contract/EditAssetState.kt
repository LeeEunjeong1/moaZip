package com.moazip.feature.assets.editasset.contract

import com.moazip.core.model.HouseholdMember
import com.moazip.core.presentation.mvi.UiState
import com.moazip.feature.assets.addasset.contract.AssetCategory
import com.moazip.feature.assets.addasset.contract.AssetType
import com.moazip.feature.assets.addasset.contract.OwnerSelection

data class EditAssetState(
    val name: String = "",
    val members: List<HouseholdMember> = emptyList(),
    val owner: OwnerSelection = OwnerSelection.Common,
    val assetType: AssetType = AssetType.ASSET,
    val category: AssetCategory? = null,
    val amount: String = "",
    val memo: String = "",
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val error: EditAssetError? = null,
) : UiState {
    val canSave get() = !isLoading && !isSaving && !isDeleting && name.isNotBlank() && category != null && amount.toLongOrNull() != null
    val availableCategories get() = when (assetType) {
        AssetType.ASSET -> listOf(AssetCategory.LEASE_DEPOSIT, AssetCategory.DEPOSIT, AssetCategory.SAVINGS, AssetCategory.CHECKING, AssetCategory.RETIREMENT, AssetCategory.HOUSING_SUBSCRIPTION, AssetCategory.OTHER)
        AssetType.INVESTMENT -> listOf(AssetCategory.ISA, AssetCategory.OVERSEAS_STOCK, AssetCategory.DOMESTIC_STOCK, AssetCategory.OTHER)
        AssetType.DEBT -> listOf(AssetCategory.LOAN, AssetCategory.OTHER)
    }
}
