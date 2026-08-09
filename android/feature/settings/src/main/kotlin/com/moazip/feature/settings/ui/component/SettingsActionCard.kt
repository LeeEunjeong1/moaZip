package com.moazip.feature.settings.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipCard
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.settings.R

@Composable
internal fun SettingsActionCard(
    onReissueInviteCode: () -> Unit,
    onLogout: () -> Unit,
) {
    MoaZipCard {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            SettingsActionItem(
                text = stringResource(R.string.settings_reissue_invite_code),
                onClick = onReissueInviteCode,
            )
            SettingsActionItem(
                text = stringResource(R.string.settings_logout),
                color = MaterialTheme.colorScheme.error,
                onClick = onLogout,
            )
        }
    }
}

@Composable
private fun SettingsActionItem(
    text: String,
    onClick: () -> Unit,
    color: androidx.compose.ui.graphics.Color = MoaZipPalette.Gray900,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Medium,
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
    )
}
