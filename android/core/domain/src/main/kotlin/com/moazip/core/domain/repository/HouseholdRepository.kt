package com.moazip.core.domain.repository

import com.moazip.core.model.HouseholdCreationResult
import com.moazip.core.model.JoinHouseholdResult

interface HouseholdRepository {
    suspend fun createHousehold(
        ownerUserId: String,
        householdName: String,
    ): HouseholdCreationResult

    suspend fun hasJoinedHousehold(userId: String): Boolean

    suspend fun reissueInviteCode(
        ownerUserId: String,
        currentInviteCode: String,
    ): String

    suspend fun getLatestInviteCode(ownerUserId: String): String?

    suspend fun joinHouseholdWithInviteCode(
        userId: String,
        inviteCode: String,
        replaceExistingHousehold: Boolean,
    ): JoinHouseholdResult
}
