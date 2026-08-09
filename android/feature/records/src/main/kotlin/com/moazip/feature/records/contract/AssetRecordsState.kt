package com.moazip.feature.records.contract

import com.moazip.core.presentation.mvi.UiState

data class AssetRecordsState(
    val selectedQuarter: String = "2026년 3분기",
    val netWorth: Long = 124_500_000,
    val previousQuarterDifference: Long = 8_500_000,
    val assetTotal: Long = 98_000_000,
    val depositTotal: Long = 87_000_000,
    val liabilityTotal: Long = 62_500_000,
    val quarterRecords: List<QuarterRecordUiModel> = listOf(
        QuarterRecordUiModel("25Q3", 62_000_000),
        QuarterRecordUiModel("25Q4", 78_000_000),
        QuarterRecordUiModel("26Q1", 92_000_000),
        QuarterRecordUiModel("26Q2", 116_000_000),
        QuarterRecordUiModel("26Q3", 124_500_000),
    ),
) : UiState
