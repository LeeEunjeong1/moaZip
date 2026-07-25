package com.moazip.core.domain.usecase

import com.moazip.core.domain.repository.HouseholdRepository
import com.moazip.core.model.HouseholdCreationResult

class CreateHouseholdUseCase(
    private val repository: HouseholdRepository,
) {
    suspend operator fun invoke(
        ownerUserId: String,
        householdName: String,
    ): HouseholdCreationResult = repository.createHousehold(
        ownerUserId = ownerUserId,
        householdName = householdName,
    )
}
