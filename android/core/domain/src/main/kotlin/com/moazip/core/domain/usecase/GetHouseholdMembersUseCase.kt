package com.moazip.core.domain.usecase

import com.moazip.core.domain.repository.HouseholdRepository
import com.moazip.core.model.HouseholdMember

class GetHouseholdMembersUseCase(
    private val repository: HouseholdRepository,
) {
    suspend operator fun invoke(userId: String): List<HouseholdMember> =
        repository.getHouseholdMembers(userId)
}
