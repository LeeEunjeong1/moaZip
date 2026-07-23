package com.moazip.app

import com.moazip.core.data.InMemoryDashboardRepository
import com.moazip.core.domain.usecase.CreateHouseholdUseCase
import com.moazip.core.domain.usecase.ObserveDashboardSummary
import com.moazip.core.firebase.FirebaseHouseholdRepository
import com.moazip.core.firebase.FirebaseUserRepository

class AppContainer {
    private val dashboardRepository = InMemoryDashboardRepository()
    private val householdRepository = FirebaseHouseholdRepository()
    val userRepository = FirebaseUserRepository()
    val createHouseholdUseCase = CreateHouseholdUseCase(householdRepository)
    val observeDashboardSummary = ObserveDashboardSummary(dashboardRepository)
}
