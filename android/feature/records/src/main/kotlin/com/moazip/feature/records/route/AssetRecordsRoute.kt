package com.moazip.feature.records.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.moazip.feature.records.AssetRecordsViewModel
import com.moazip.feature.records.ui.AssetRecordsScreen

@Composable
fun AssetRecordsRoute(
    modifier: Modifier = Modifier,
    viewModel: AssetRecordsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    AssetRecordsScreen(state = state, onIntent = viewModel::onIntent, modifier = modifier)
}
