package com.moazip.feature.records.contract

data class QuarterRecordUiModel(
    val label: String,
    val netWorth: Long,
    val growthRate: Double? = null,
)
