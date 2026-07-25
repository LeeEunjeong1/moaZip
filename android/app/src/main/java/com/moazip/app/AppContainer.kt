package com.moazip.app

import com.moazip.core.data.InMemoryDashboardRepository
import com.moazip.core.domain.usecase.CreateHouseholdUseCase
import com.moazip.core.domain.usecase.AddAssetUseCase
import com.moazip.core.domain.usecase.GetLatestInviteCodeUseCase
import com.moazip.core.domain.usecase.HasJoinedHouseholdUseCase
import com.moazip.core.domain.usecase.JoinHouseholdWithInviteCodeUseCase
import com.moazip.core.domain.usecase.ObserveDashboardSummary
import com.moazip.core.domain.usecase.ReissueInviteCodeUseCase
import com.moazip.core.firebase.FirebaseHouseholdRepository
import com.moazip.core.firebase.FirebaseAssetRepository
import com.moazip.core.firebase.FirebaseUserRepository

class AppContainer {
    private val dashboardRepository = InMemoryDashboardRepository()
    private val householdRepository = FirebaseHouseholdRepository()
    private val assetRepository = FirebaseAssetRepository()
    val userRepository = FirebaseUserRepository()
    val createHouseholdUseCase = CreateHouseholdUseCase(householdRepository)
    val addAssetUseCase = AddAssetUseCase(assetRepository)
    val hasJoinedHouseholdUseCase = HasJoinedHouseholdUseCase(householdRepository)
    val joinHouseholdWithInviteCodeUseCase = JoinHouseholdWithInviteCodeUseCase(householdRepository)
    val reissueInviteCodeUseCase = ReissueInviteCodeUseCase(householdRepository)
    val getLatestInviteCodeUseCase = GetLatestInviteCodeUseCase(householdRepository)
    val observeDashboardSummary = ObserveDashboardSummary(dashboardRepository)
}
