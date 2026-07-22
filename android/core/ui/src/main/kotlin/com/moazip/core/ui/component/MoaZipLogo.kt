package com.moazip.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.theme.MoaZipPalette

@Composable
fun MoaZipLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(84.dp)
            .background(
                color = MoaZipPalette.Yellow500,
                shape = RoundedCornerShape(27.dp),
            ),
    )
}
