package com.moazip.feature.assets.assetlist.route

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.moazip.feature.assets.assetlist.AssetListViewModel
import com.moazip.feature.assets.assetlist.ui.AssetListScreen
import com.moazip.feature.assets.assetlist.contract.AssetListEffect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun AssetListRoute(
    modifier: Modifier = Modifier,
    viewModel: AssetListViewModel = hiltViewModel(),
    onEditAsset: (String) -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(viewModel) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is AssetListEffect.NavigateToEditAsset -> onEditAsset(effect.assetId)
            }
        }.launchIn(this)
    }
    AssetListScreen(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}
