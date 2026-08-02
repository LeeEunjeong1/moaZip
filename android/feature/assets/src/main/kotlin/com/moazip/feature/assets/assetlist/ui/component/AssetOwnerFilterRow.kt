package com.moazip.feature.assets.assetlist.ui.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
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
import com.moazip.feature.assets.assetlist.contract.AssetOwnerFilter

@Composable
internal fun AssetOwnerFilterRow(
    filters: List<AssetOwnerFilter>,
    selectedFilter: AssetOwnerFilter,
    onFilterSelected: (AssetOwnerFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        filters.forEach { filter ->
            val selected = selectedFilter == filter
            FilterChip(
                selected = selected,
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
                    selected = selected,
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
private fun AssetOwnerFilter.label(): String = when (this) {
    AssetOwnerFilter.All -> stringResource(R.string.asset_list_filter_all)
    AssetOwnerFilter.Common -> stringResource(R.string.add_asset_owner_common)
    is AssetOwnerFilter.Member -> displayName
}
