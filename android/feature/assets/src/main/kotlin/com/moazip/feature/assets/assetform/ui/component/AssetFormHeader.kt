package com.moazip.feature.assets.assetform.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moazip.core.ui.theme.MoaZipPalette

@Composable
internal fun AssetFormHeader(
    title: String,
    description: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            color = MoaZipPalette.Gray900,
            fontSize = 26.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = description,
            color = MoaZipPalette.Gray500,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
