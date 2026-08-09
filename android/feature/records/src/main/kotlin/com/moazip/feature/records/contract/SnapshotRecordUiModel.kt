package com.moazip.feature.records.contract

data class SnapshotRecordUiModel(
    val id: String,
    val recordedDate: String,
    val netWorth: Long,
    val financialAssetTotal: Long,
    val depositTotal: Long,
    val liabilityTotal: Long,
    val growthRate: Double? = null,
    val growthAmount: Long? = null,
    val liabilityChange: Long? = null,
)
