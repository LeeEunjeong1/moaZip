package com.moazip.feature.records.contract

import com.moazip.core.presentation.mvi.UiState

data class AssetRecordsState(
    val selectedQuarter: String = "",
    val netWorth: Long = 0L,
    val previousRecordGrowthRate: Double? = null,
    val quarterRecords: List<QuarterRecordUiModel> = emptyList(),
    val snapshotRecords: List<SnapshotRecordUiModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: AssetRecordsError? = null,
) : UiState
