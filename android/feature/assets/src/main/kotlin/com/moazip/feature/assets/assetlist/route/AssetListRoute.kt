package com.moazip.feature.assets.assetlist.route

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moazip.feature.assets.assetlist.contract.AssetListIntent
import com.moazip.feature.assets.assetlist.contract.AssetListState
import com.moazip.feature.assets.assetlist.ui.AssetListScreen

@Composable
fun AssetListRoute(
    modifier: Modifier = Modifier,
    state: AssetListState = AssetListState(),
    onIntent: (AssetListIntent) -> Unit = {},
) {
    AssetListScreen(
        state = state,
        onIntent = onIntent,
        modifier = modifier,
    )
}
