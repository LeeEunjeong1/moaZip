package com.moazip.feature.assets.assetlist.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
        modifier = Modifier
            .fillMaxWidth()
            .background(MoaZipPalette.Gray50, RoundedCornerShape(14.dp)),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        AssetListFilter.entries.forEach { filter ->
            val selected = selectedFilter == filter
            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(if (selected) MoaZipPalette.Yellow500 else MoaZipPalette.Gray50)
                    .clickable { onFilterSelected(filter) }
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = filter.label(),
                    color = if (selected) MoaZipPalette.Gray950 else MoaZipPalette.Gray500,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
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
