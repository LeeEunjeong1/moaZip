package com.moazip.feature.assets.assetlist.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.assets.R
import com.moazip.feature.assets.assetlist.contract.AssetListFilter

@Composable
internal fun AssetFilterRow(
    selectedFilter: AssetListFilter,
    onFilterSelected: (AssetListFilter) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AssetListFilter.entries.forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = filter.label(),
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
                shape = RoundedCornerShape(12.dp),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selectedFilter == filter,
                    borderColor = MoaZipPalette.Beige200,
                    selectedBorderColor = MoaZipPalette.Yellow500,
                    borderWidth = 1.dp,
                    selectedBorderWidth = 1.dp,
                ),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MoaZipPalette.White,
                    labelColor = MoaZipPalette.Gray500,
                    selectedContainerColor = MoaZipPalette.Yellow500,
                    selectedLabelColor = MoaZipPalette.Gray950,
                ),
            )
        }
    }
}

@Composable
private fun AssetListFilter.label(): String = stringResource(
    when (this) {
        AssetListFilter.ALL -> R.string.asset_list_filter_all
        AssetListFilter.ASSET -> R.string.asset_list_asset
        AssetListFilter.INVESTMENT -> R.string.asset_list_investment
        AssetListFilter.LIABILITY -> R.string.asset_list_liability
    },
)
