package com.moazip.feature.records.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipButton
import com.moazip.feature.records.R
import com.moazip.feature.records.contract.AssetRecordsError

@Composable
internal fun AssetRecordsErrorContent(error: AssetRecordsError, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 56.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            stringResource(
                if (error == AssetRecordsError.UNAUTHENTICATED) R.string.asset_records_error_unauthenticated
                else R.string.asset_records_error_load_failed,
            ),
        )
        MoaZipButton(
            text = stringResource(R.string.asset_records_retry),
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth(0.5f),
        )
    }
}
