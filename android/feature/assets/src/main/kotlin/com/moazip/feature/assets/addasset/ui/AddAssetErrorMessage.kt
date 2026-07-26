package com.moazip.feature.assets.addasset.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.moazip.feature.assets.R
import com.moazip.feature.assets.addasset.contract.AddAssetError

@Composable
internal fun AddAssetError.toMessage(): String = when (this) {
    AddAssetError.INVALID_INPUT -> stringResource(R.string.add_asset_error_invalid_input)
    AddAssetError.UNAUTHENTICATED -> stringResource(R.string.add_asset_error_unauthenticated)
    AddAssetError.SAVE_FAILED -> stringResource(R.string.add_asset_error_save_failed)
}
