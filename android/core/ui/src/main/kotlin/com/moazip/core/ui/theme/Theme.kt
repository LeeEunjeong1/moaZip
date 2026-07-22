package com.moazip.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val MoaZipColorScheme = lightColorScheme(
    primary = MoaZipPalette.Yellow500,
    onPrimary = MoaZipPalette.Gray950,
    primaryContainer = MoaZipPalette.Yellow50,
    onPrimaryContainer = MoaZipPalette.Gray900,
    background = MoaZipPalette.Gray50,
    onBackground = MoaZipPalette.Gray900,
    surface = MoaZipPalette.White,
    onSurface = MoaZipPalette.Gray900,
    surfaceVariant = MoaZipPalette.Yellow50,
    onSurfaceVariant = MoaZipPalette.Gray500,
    outline = MoaZipPalette.Beige200,
    tertiary = MoaZipPalette.Green600,
)

@Composable
fun MoaZipTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MoaZipColorScheme,
        typography = MoaZipTypography,
        shapes = MoaZipShapes,
        content = content,
    )
}
