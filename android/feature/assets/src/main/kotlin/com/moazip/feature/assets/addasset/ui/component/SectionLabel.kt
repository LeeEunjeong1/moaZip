package com.moazip.feature.assets.addasset.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.moazip.core.ui.theme.MoaZipPalette

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        color = MoaZipPalette.Gray900,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
    )
}
