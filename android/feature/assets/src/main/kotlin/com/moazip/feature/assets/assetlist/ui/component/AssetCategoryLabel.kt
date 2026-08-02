package com.moazip.feature.assets.assetlist.ui.component

import androidx.annotation.StringRes
import com.moazip.core.model.AssetCategory
import com.moazip.feature.assets.R

@StringRes
internal fun AssetCategory.labelRes(): Int = when (this) {
    AssetCategory.LEASE_DEPOSIT -> R.string.add_asset_category_lease_deposit
    AssetCategory.DEPOSIT -> R.string.add_asset_category_deposit
    AssetCategory.SAVINGS -> R.string.add_asset_category_savings
    AssetCategory.LOAN -> R.string.add_asset_category_loan
    AssetCategory.CHECKING -> R.string.add_asset_category_checking
    AssetCategory.RETIREMENT -> R.string.add_asset_category_retirement
    AssetCategory.HOUSING_SUBSCRIPTION -> R.string.add_asset_category_housing_subscription
    AssetCategory.ISA -> R.string.add_asset_category_isa
    AssetCategory.OVERSEAS_STOCK -> R.string.add_asset_category_overseas_stock
    AssetCategory.DOMESTIC_STOCK -> R.string.add_asset_category_domestic_stock
    AssetCategory.CASH,
    AssetCategory.STOCK,
    AssetCategory.DIVIDEND,
    AssetCategory.OTHER,
    AssetCategory.ETC,
    -> R.string.add_asset_category_other
}
