package com.moazip.feature.assets.assetlist.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipButton
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.assets.R
import com.moazip.feature.assets.assetlist.contract.AssetListError

@Composable
internal fun AssetListErrorContent(
    error: AssetListError,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(
                when (error) {
                    AssetListError.UNAUTHENTICATED -> R.string.asset_list_error_unauthenticated
                    AssetListError.LOAD_FAILED -> R.string.asset_list_error_load_failed
                },
            ),
            color = MoaZipPalette.Gray500,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
        MoaZipButton(
            text = stringResource(R.string.asset_list_retry),
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth(0.5f),
        )
    }
}
