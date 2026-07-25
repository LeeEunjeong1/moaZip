package com.moazip.feature.assets.addasset.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.assets.R
import com.moazip.feature.assets.addasset.contract.AssetCategory
import com.moazip.feature.assets.addasset.contract.AssetType

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategorySelector(
    selectedType: AssetType,
    selectedCategory: AssetCategory?,
    availableCategories: List<AssetCategory>,
    onTypeSelected: (AssetType) -> Unit,
    onCategorySelected: (AssetCategory) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionLabel(text = stringResource(R.string.add_asset_category_label))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AssetType.entries.forEach { assetType ->
                FilterChip(
                    selected = selectedType == assetType,
                    onClick = { onTypeSelected(assetType) },
                    label = { Text(stringResource(assetType.labelRes())) },
                    colors = categoryChipColors(),
                )
            }
        }
        Text(
            text = stringResource(R.string.add_asset_detail_category_label),
            color = MoaZipPalette.Gray500,
            style = MaterialTheme.typography.bodySmall,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            availableCategories.forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { onCategorySelected(category) },
                    label = { Text(stringResource(category.labelRes())) },
                    colors = categoryChipColors(),
                )
            }
        }
    }
}

@Composable
private fun categoryChipColors() = FilterChipDefaults.filterChipColors(
    containerColor = MoaZipPalette.White,
    labelColor = MoaZipPalette.Gray900,
    selectedContainerColor = MoaZipPalette.Yellow500,
    selectedLabelColor = MoaZipPalette.Gray950,
)

@StringRes
private fun AssetType.labelRes(): Int = when (this) {
    AssetType.ASSET -> R.string.add_asset_type_asset
    AssetType.INVESTMENT -> R.string.add_asset_type_investment
    AssetType.DEBT -> R.string.add_asset_type_debt
}

@StringRes
private fun AssetCategory.labelRes(): Int = when (this) {
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
    AssetCategory.OTHER -> R.string.add_asset_category_other
}
