package com.moazip.core.data

import com.moazip.core.domain.repository.DashboardRepository
import com.moazip.core.model.DashboardSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Replace with a Firestore-backed implementation without changing feature modules. */
class InMemoryDashboardRepository : DashboardRepository {
    private val summary = MutableStateFlow(
        DashboardSummary(
            netWorth = 320_000_000,
            quarterlyGrowthRate = 4.8,
            financialAssets = 185_000_000,
            investmentProfitLoss = 12_400_000,
            monthlySavings = 4_500_000,
            homeGoalAmount = 600_000_000,
            homeGoalProgress = 0.53f,
        ),
    )

    override fun observeSummary() = summary.asStateFlow()
}
