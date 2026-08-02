package com.moazip.feature.assets.assetlist.route

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.moazip.feature.assets.assetlist.AssetListViewModel
import com.moazip.feature.assets.assetlist.ui.AssetListScreen

@Composable
fun AssetListRoute(
    modifier: Modifier = Modifier,
    viewModel: AssetListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    AssetListScreen(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}
