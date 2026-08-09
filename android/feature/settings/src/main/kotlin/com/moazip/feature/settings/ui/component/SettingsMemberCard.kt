package com.moazip.feature.settings.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipCard
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.settings.R
import com.moazip.feature.settings.contract.SettingsMemberRole
import com.moazip.feature.settings.contract.SettingsMemberUiModel

@Composable
internal fun SettingsMemberCard(members: List<SettingsMemberUiModel>) {
    MoaZipCard {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
                text = stringResource(R.string.settings_members_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MoaZipPalette.Gray900,
            )
            members.forEach { member -> SettingsMemberRow(member = member) }
        }
    }
}

@Composable
private fun SettingsMemberRow(member: SettingsMemberUiModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .background(
                    if (member.role == SettingsMemberRole.OWNER) {
                        MoaZipPalette.Yellow500
                    } else {
                        MoaZipPalette.Yellow50
                    },
                ),
        )
        Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text(
                text = member.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MoaZipPalette.Gray900,
            )
            Text(
                text = stringResource(
                    if (member.role == SettingsMemberRole.OWNER) {
                        R.string.settings_role_owner
                    } else {
                        R.string.settings_role_member
                    },
                ),
                style = MaterialTheme.typography.labelSmall,
                color = MoaZipPalette.Gray500,
            )
        }
    }
}
