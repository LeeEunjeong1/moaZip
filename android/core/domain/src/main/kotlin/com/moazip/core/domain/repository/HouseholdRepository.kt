package com.moazip.core.domain.repository

import com.moazip.core.model.HouseholdCreationResult

interface HouseholdRepository {
    suspend fun createHousehold(
        ownerUserId: String,
        householdName: String,
    ): HouseholdCreationResult
}
