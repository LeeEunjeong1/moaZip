package com.moazip.feature.records

import com.moazip.core.presentation.mvi.MviViewModel
import com.moazip.feature.records.contract.AssetRecordsEffect
import com.moazip.feature.records.contract.AssetRecordsIntent
import com.moazip.feature.records.contract.AssetRecordsState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AssetRecordsViewModel @Inject constructor() :
    MviViewModel<AssetRecordsIntent, AssetRecordsState, AssetRecordsEffect>(AssetRecordsState()) {
    override fun onIntent(intent: AssetRecordsIntent) = Unit
}
