package com.moazip.feature.assets.assetlist.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipButton
import com.moazip.core.ui.component.MoaZipCard
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.assets.R

@Composable
internal fun AssetSnapshotRecordCard(
    isRecorded: Boolean,
    isRecording: Boolean,
    showError: Boolean,
    onRecordClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isCollapsed by rememberSaveable { mutableStateOf(true) }

    if (isRecorded && isCollapsed) {
        Surface(
            onClick = { isCollapsed = false },
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
            shadowElevation = 2.dp,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = MoaZipPalette.Yellow500,
                )
                Text(
                    text = stringResource(R.string.asset_snapshot_recorded_title),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    imageVector = Icons.Outlined.ExpandMore,
                    contentDescription = stringResource(R.string.asset_snapshot_expand),
                    tint = MoaZipPalette.Gray500,
                )
            }
        }
        return
    }

    MoaZipCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(
                        if (isRecorded) R.string.asset_snapshot_recorded_title
                        else R.string.asset_snapshot_record_title,
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                if (isRecorded) {
                    IconButton(onClick = { isCollapsed = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = stringResource(R.string.asset_snapshot_collapse),
                            tint = MoaZipPalette.Gray500,
                        )
                    }
                }
            }
            if (showError) {
                Text(
                    text = stringResource(R.string.asset_snapshot_error_record_failed),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Text(
                text = stringResource(R.string.asset_snapshot_record_description),
                style = MaterialTheme.typography.bodySmall,
            )
            MoaZipButton(
                text = stringResource(
                    when {
                        isRecording -> R.string.asset_snapshot_recording
                        isRecorded -> R.string.asset_snapshot_record_again
                        else -> R.string.asset_snapshot_record_button
                    },
                ),
                enabled = !isRecording,
                onClick = onRecordClick,
            )
        }
    }
}
