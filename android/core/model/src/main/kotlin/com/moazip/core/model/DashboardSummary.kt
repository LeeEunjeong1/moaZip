package com.moazip.core.model

data class DashboardSummary(
    val assetTotal: Long = 0,
    val investmentTotal: Long = 0,
    val liabilityTotal: Long = 0,
) {
    val netWorth: Long
        get() = assetTotal + investmentTotal - liabilityTotal
}
