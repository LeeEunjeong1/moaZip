package com.moazip.core.domain.usecase

import com.moazip.core.domain.repository.HouseholdRepository

class HasJoinedHouseholdUseCase(
    private val repository: HouseholdRepository,
) {
    suspend operator fun invoke(userId: String): Boolean = repository.hasJoinedHousehold(userId)
}
