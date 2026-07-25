package com.moazip.core.domain.usecase

import com.moazip.core.domain.repository.HouseholdRepository

class GetLatestInviteCodeUseCase(
    private val repository: HouseholdRepository,
) {
    suspend operator fun invoke(ownerUserId: String): String? =
        repository.getLatestInviteCode(ownerUserId)
}
