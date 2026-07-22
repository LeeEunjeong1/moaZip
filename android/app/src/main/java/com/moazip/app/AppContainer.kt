package com.moazip.app

import com.moazip.core.data.InMemoryDashboardRepository
import com.moazip.core.domain.usecase.ObserveDashboardSummary

class AppContainer {
    private val dashboardRepository = InMemoryDashboardRepository()
    val observeDashboardSummary = ObserveDashboardSummary(dashboardRepository)
}
