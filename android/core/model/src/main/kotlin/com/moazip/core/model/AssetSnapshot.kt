package com.moazip.core.model

data class AssetSnapshot(
    val id: String,
    val monthKey: String,
    val assetTotal: Long,
    val investmentTotal: Long,
    val liabilityTotal: Long,
    val financialAssetTotal: Long = assetTotal + investmentTotal,
    val depositTotal: Long = 0L,
    val recordedAtMillis: Long,
) {
    val netWorth: Long
        get() = assetTotal + investmentTotal - liabilityTotal
}
