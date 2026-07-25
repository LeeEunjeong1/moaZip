package com.moazip.feature.assets.addasset.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.moazip.feature.assets.addasset.AddAssetViewModel
import com.moazip.feature.assets.addasset.ui.AddAssetScreen

@Composable
fun AddAssetRoute(
    viewModel: AddAssetViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()

    AddAssetScreen(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}
