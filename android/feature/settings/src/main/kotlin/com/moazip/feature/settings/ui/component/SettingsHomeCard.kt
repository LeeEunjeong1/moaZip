package com.moazip.feature.settings.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipCard
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.settings.R

@Composable
internal fun SettingsHomeCard(householdName: String) {
    MoaZipCard {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = stringResource(R.string.settings_home_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MoaZipPalette.Gray500,
            )
            Text(
                text = householdName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MoaZipPalette.Gray900,
            )
        }
    }
}
