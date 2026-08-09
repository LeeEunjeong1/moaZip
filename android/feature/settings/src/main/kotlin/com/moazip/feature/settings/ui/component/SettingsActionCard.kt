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
    inviteCode: String?,
    canReissueInviteCode: Boolean,
    isReissuingInviteCode: Boolean,
    reissueFailed: Boolean,
    onReissueInviteCode: () -> Unit,
    onLogout: () -> Unit,
) {
    MoaZipCard {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            if (canReissueInviteCode) {
                SettingsActionItem(
                    text = if (isReissuingInviteCode) {
                        stringResource(R.string.settings_reissuing_invite_code)
                    } else {
                        stringResource(R.string.settings_reissue_invite_code)
                    },
                    supportingText = when {
                        reissueFailed -> stringResource(R.string.settings_reissue_failed)
                        inviteCode != null -> stringResource(R.string.settings_current_invite_code, inviteCode)
                        else -> null
                    },
                    supportingTextIsError = reissueFailed,
                    onClick = onReissueInviteCode,
                    enabled = !isReissuingInviteCode,
                )
            }
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
    supportingText: String? = null,
    supportingTextIsError: Boolean = false,
    enabled: Boolean = true,
    color: androidx.compose.ui.graphics.Color = MoaZipPalette.Gray900,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = color,
        )
        supportingText?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = if (supportingTextIsError) MaterialTheme.colorScheme.error else MoaZipPalette.Gray500,
            )
        }
    }
}
