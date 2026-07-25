package com.moazip.feature.assets.addasset.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipButton
import com.moazip.core.ui.component.MoaZipOutlinedButton
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.assets.R

@Composable
fun AddAssetActionButtons(
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MoaZipPalette.Cream50)
            .padding(horizontal = 24.dp)
            .padding(top = 8.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        MoaZipButton(
            text = stringResource(R.string.add_asset_save),
            onClick = onSaveClick,
        )
        MoaZipOutlinedButton(
            text = stringResource(R.string.add_asset_cancel),
            onClick = onCancelClick,
        )
    }
}
