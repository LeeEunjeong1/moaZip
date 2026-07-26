package com.moazip.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.moazip.core.ui.theme.MoaZipPalette

@Composable
internal fun HouseholdGateScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MoaZipPalette.Cream50),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}
