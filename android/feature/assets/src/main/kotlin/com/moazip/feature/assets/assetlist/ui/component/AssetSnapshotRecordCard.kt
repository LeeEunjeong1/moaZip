package com.moazip.feature.assets.assetlist.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipButton
import com.moazip.core.ui.component.MoaZipCard
import com.moazip.feature.assets.R

@Composable
internal fun AssetSnapshotRecordCard(
    isRecorded: Boolean,
    isRecording: Boolean,
    showError: Boolean,
    onRecordClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MoaZipCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = stringResource(
                    if (isRecorded) R.string.asset_snapshot_recorded_title
                    else R.string.asset_snapshot_record_title,
                ),
                style = MaterialTheme.typography.titleMedium,
            )
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
