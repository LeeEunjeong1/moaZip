package com.moazip.app

import com.moazip.core.data.InMemoryDashboardRepository
import com.moazip.core.domain.usecase.ObserveDashboardSummary
import com.moazip.core.firebase.FirebaseUserRepository

class AppContainer {
    private val dashboardRepository = InMemoryDashboardRepository()
    val userRepository = FirebaseUserRepository()
    val observeDashboardSummary = ObserveDashboardSummary(dashboardRepository)
}
