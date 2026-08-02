package com.moazip.feature.assets.assetlist.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.assets.R

@Composable
internal fun AssetListHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = stringResource(R.string.asset_list_title),
            color = MoaZipPalette.Gray900,
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(R.string.asset_list_description),
            color = MoaZipPalette.Gray500,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
