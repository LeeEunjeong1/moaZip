package com.moazip.feature.assets.editasset.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.moazip.feature.assets.editasset.EditAssetViewModel
import com.moazip.feature.assets.editasset.ui.EditAssetScreen

@Composable
fun EditAssetRoute(viewModel: EditAssetViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.state.collectAsState()
    EditAssetScreen(state = state, onIntent = viewModel::onIntent, modifier = modifier)
}
