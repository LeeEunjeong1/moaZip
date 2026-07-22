package com.moazip.core.model

data class DashboardSummary(
    val netWorth: Long = 0,
    val quarterlyGrowthRate: Double = 0.0,
    val financialAssets: Long = 0,
    val investmentProfitLoss: Long = 0,
    val monthlySavings: Long = 0,
    val homeGoalAmount: Long = 0,
    val homeGoalProgress: Float = 0f,
)
