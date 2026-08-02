package com.moazip.feature.assets.assetlist.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.assets.R
import com.moazip.feature.assets.assetlist.contract.AssetSortOption

@Composable
internal fun AssetListToolbar(
    assetCount: Int,
    selectedSortOption: AssetSortOption,
    onSortOptionSelected: (AssetSortOption) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.asset_list_count, assetCount),
            color = MoaZipPalette.Gray500,
            style = MaterialTheme.typography.bodyMedium,
        )
        AssetSortButton(
            selectedOption = selectedSortOption,
            onOptionSelected = onSortOptionSelected,
        )
    }
}
