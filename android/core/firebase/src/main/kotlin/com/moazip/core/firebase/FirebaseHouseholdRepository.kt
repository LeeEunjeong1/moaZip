package com.moazip.core.firebase

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.moazip.core.domain.repository.HouseholdRepository
import com.moazip.core.model.HouseholdCreationResult
import kotlinx.coroutines.tasks.await
import kotlin.random.Random

class FirebaseHouseholdRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : HouseholdRepository {
    override suspend fun createHousehold(
        ownerUserId: String,
        householdName: String,
    ): HouseholdCreationResult {
        val householdDocument = firestore.collection(HOUSEHOLDS_COLLECTION).document()
        val inviteCode = generateInviteCode()
        val memberDocument = householdDocument.collection(MEMBERS_COLLECTION).document(ownerUserId)
        val inviteCodeDocument = firestore.collection(INVITE_CODES_COLLECTION).document(inviteCode)
        val expiresAt = Timestamp((System.currentTimeMillis() / MILLIS_PER_SECOND) + INVITE_TTL_SECONDS, 0)

        firestore.batch()
            .set(
                householdDocument,
                mapOf(
                    ID_FIELD to householdDocument.id,
                    NAME_FIELD to householdName.trim(),
                    OWNER_ID_FIELD to ownerUserId,
                    INVITE_CODE_FIELD to inviteCode,
                    CREATED_AT_FIELD to FieldValue.serverTimestamp(),
                    UPDATED_AT_FIELD to FieldValue.serverTimestamp(),
                ),
            )
            .set(
                memberDocument,
                mapOf(
                    ID_FIELD to ownerUserId,
                    HOUSEHOLD_ID_FIELD to householdDocument.id,
                    ROLE_FIELD to OWNER_ROLE,
                    CREATED_AT_FIELD to FieldValue.serverTimestamp(),
                    UPDATED_AT_FIELD to FieldValue.serverTimestamp(),
                ),
            )
            .set(
                inviteCodeDocument,
                mapOf(
                    CODE_FIELD to inviteCode,
                    HOUSEHOLD_ID_FIELD to householdDocument.id,
                    CREATED_BY_FIELD to ownerUserId,
                    EXPIRES_AT_FIELD to expiresAt,
                    CREATED_AT_FIELD to FieldValue.serverTimestamp(),
                ),
            )
            .commit()
            .await()

        return HouseholdCreationResult(
            householdId = householdDocument.id,
            inviteCode = inviteCode,
        )
    }

    private fun generateInviteCode(): String {
        val suffix = (1..INVITE_CODE_SUFFIX_LENGTH)
            .map { INVITE_CODE_CHARS.random(Random.Default) }
            .joinToString(separator = "")
        return "$INVITE_CODE_PREFIX-$suffix"
    }

    private companion object {
        const val HOUSEHOLDS_COLLECTION = "households"
        const val MEMBERS_COLLECTION = "members"
        const val INVITE_CODES_COLLECTION = "inviteCodes"
        const val ID_FIELD = "id"
        const val NAME_FIELD = "name"
        const val OWNER_ID_FIELD = "ownerId"
        const val INVITE_CODE_FIELD = "inviteCode"
        const val HOUSEHOLD_ID_FIELD = "householdId"
        const val ROLE_FIELD = "role"
        const val CODE_FIELD = "code"
        const val CREATED_BY_FIELD = "createdBy"
        const val EXPIRES_AT_FIELD = "expiresAt"
        const val CREATED_AT_FIELD = "createdAt"
        const val UPDATED_AT_FIELD = "updatedAt"
        const val OWNER_ROLE = "owner"
        const val INVITE_CODE_PREFIX = "MZ"
        const val INVITE_CODE_SUFFIX_LENGTH = 4
        const val INVITE_TTL_SECONDS = 24 * 60 * 60
        const val MILLIS_PER_SECOND = 1_000
        const val INVITE_CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
    }
}
