package com.moazip.feature.assets.assetlist.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.assets.R
import com.moazip.feature.assets.assetlist.contract.AssetListFilter
import com.moazip.feature.assets.assetlist.contract.AssetListItemUiModel

@Composable
internal fun AssetListItem(
    asset: AssetListItemUiModel,
    onClick: () -> Unit,
) {
    val categoryName = stringResource(asset.category.labelRes())
    val ownerName = asset.ownerName ?: stringResource(R.string.add_asset_owner_common)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MoaZipPalette.White, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(asset.kind.backgroundColor(), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = categoryName.take(1),
                color = MoaZipPalette.Gray900,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            Text(
                text = asset.name,
                color = MoaZipPalette.Gray900,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(
                    R.string.asset_list_item_description,
                    categoryName,
                    ownerName,
                ),
                color = MoaZipPalette.Gray500,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Text(
            text = stringResource(
                R.string.asset_list_amount_won,
                asset.amount.toFormattedAmount(),
            ),
            color = MoaZipPalette.Gray900,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

private fun AssetListFilter.backgroundColor() = when (this) {
    AssetListFilter.ALL,
    AssetListFilter.ASSET,
    -> MoaZipPalette.Yellow50
    AssetListFilter.INVESTMENT -> MoaZipPalette.Gray50
    AssetListFilter.LIABILITY -> MoaZipPalette.Beige200
}
