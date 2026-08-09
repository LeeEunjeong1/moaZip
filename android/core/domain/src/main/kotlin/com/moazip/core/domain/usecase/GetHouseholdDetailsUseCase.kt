package com.moazip.core.domain.usecase

import com.moazip.core.domain.repository.HouseholdRepository
import com.moazip.core.model.HouseholdDetails

class GetHouseholdDetailsUseCase(
    private val repository: HouseholdRepository,
) {
    suspend operator fun invoke(userId: String): HouseholdDetails =
        repository.getHouseholdDetails(userId)
}
