package com.moazip.feature.assets.addasset.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.assets.R

@Composable
fun AddAssetHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = stringResource(R.string.add_asset_title),
            color = MoaZipPalette.Gray900,
            fontSize = 26.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stringResource(R.string.add_asset_description),
            color = MoaZipPalette.Gray500,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
