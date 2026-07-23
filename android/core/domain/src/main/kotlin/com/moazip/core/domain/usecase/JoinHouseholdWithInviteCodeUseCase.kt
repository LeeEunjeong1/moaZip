package com.moazip.core.domain.usecase

import com.moazip.core.domain.repository.HouseholdRepository
import com.moazip.core.model.JoinHouseholdResult

class JoinHouseholdWithInviteCodeUseCase(
    private val repository: HouseholdRepository,
) {
    suspend operator fun invoke(
        userId: String,
        inviteCode: String,
        replaceExistingHousehold: Boolean = false,
    ): JoinHouseholdResult {
        return repository.joinHouseholdWithInviteCode(
            userId = userId,
            inviteCode = inviteCode,
            replaceExistingHousehold = replaceExistingHousehold,
        )
    }
}
