package com.moazip.feature.settings.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.settings.R
import com.moazip.feature.settings.contract.SettingsError

@Composable
internal fun SettingsErrorContent(
    error: SettingsError,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(
                if (error == SettingsError.UNAUTHENTICATED) {
                    R.string.settings_error_unauthenticated
                } else {
                    R.string.settings_error_load_failed
                },
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MoaZipPalette.Gray500,
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = MoaZipPalette.Yellow500),
        ) {
            Text(text = stringResource(R.string.settings_retry), color = MoaZipPalette.Gray900)
        }
    }
}
