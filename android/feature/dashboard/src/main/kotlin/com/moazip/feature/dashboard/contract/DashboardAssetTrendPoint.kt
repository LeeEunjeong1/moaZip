package com.moazip.feature.dashboard.contract

data class DashboardAssetTrendPoint(
    val monthKey: String,
    val label: String,
    val netWorth: Long,
    val isForecast: Boolean = false,
)
