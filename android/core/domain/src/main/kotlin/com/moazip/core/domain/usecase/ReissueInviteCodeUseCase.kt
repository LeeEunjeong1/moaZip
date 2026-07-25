package com.moazip.core.domain.usecase

import com.moazip.core.domain.repository.HouseholdRepository

class ReissueInviteCodeUseCase(
    private val repository: HouseholdRepository,
) {
    suspend operator fun invoke(
        ownerUserId: String,
        currentInviteCode: String,
    ): String = repository.reissueInviteCode(
        ownerUserId = ownerUserId,
        currentInviteCode = currentInviteCode,
    )
}
