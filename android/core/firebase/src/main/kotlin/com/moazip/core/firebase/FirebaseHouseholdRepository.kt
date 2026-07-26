package com.moazip.core.firebase

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.moazip.core.domain.repository.HouseholdRepository
import com.moazip.core.model.HouseholdCreationResult
import com.moazip.core.model.HouseholdMember
import com.moazip.core.model.JoinHouseholdResult
import kotlinx.coroutines.tasks.await
import java.util.Locale
import kotlin.random.Random

class FirebaseHouseholdRepository(
    private val firestore: FirebaseFirestore,
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
        saveHouseholdId(ownerUserId, householdDocument.id)

        return HouseholdCreationResult(
            householdId = householdDocument.id,
            inviteCode = inviteCode,
        )
    }

    override suspend fun joinHouseholdWithInviteCode(
        userId: String,
        inviteCode: String,
        replaceExistingHousehold: Boolean,
    ): JoinHouseholdResult {
        val normalizedInviteCode = inviteCode.trim().uppercase(Locale.ROOT)
        val inviteCodeDocument = firestore.collection(INVITE_CODES_COLLECTION).document(normalizedInviteCode)
        val inviteCodeSnapshot = inviteCodeDocument.get().await()
        if (!inviteCodeSnapshot.exists()) {
            throw NoSuchElementException("Invite code does not exist.")
        }

        val expiresAt = inviteCodeSnapshot.getTimestamp(EXPIRES_AT_FIELD)
        if (expiresAt != null && expiresAt.seconds < Timestamp.now().seconds) {
            throw IllegalStateException("Invite code has expired.")
        }

        val householdId = inviteCodeSnapshot.getString(HOUSEHOLD_ID_FIELD)
            ?: throw IllegalStateException("Invite code is missing household id.")
        val existingMemberSnapshots = firestore
            .collectionGroup(MEMBERS_COLLECTION)
            .whereEqualTo(ID_FIELD, userId)
            .get()
            .await()
            .documents
        val existingHouseholdIds = existingMemberSnapshots.mapNotNull { it.getString(HOUSEHOLD_ID_FIELD) }

        if (householdId in existingHouseholdIds) {
            return JoinHouseholdResult.AlreadyMemberOfHousehold
        }
        if (existingHouseholdIds.isNotEmpty() && !replaceExistingHousehold) {
            return JoinHouseholdResult.RequiresHouseholdSwitch
        }

        firestore.runTransaction { transaction ->
            val memberDocument = firestore
                .collection(HOUSEHOLDS_COLLECTION)
                .document(householdId)
                .collection(MEMBERS_COLLECTION)
                .document(userId)

            existingMemberSnapshots.forEach { memberSnapshot ->
                transaction.delete(memberSnapshot.reference)
            }
            transaction.set(
                memberDocument,
                mapOf(
                    ID_FIELD to userId,
                    HOUSEHOLD_ID_FIELD to householdId,
                    ROLE_FIELD to MEMBER_ROLE,
                    JOINED_WITH_INVITE_CODE_FIELD to normalizedInviteCode,
                    CREATED_AT_FIELD to FieldValue.serverTimestamp(),
                    UPDATED_AT_FIELD to FieldValue.serverTimestamp(),
                ),
                SetOptions.merge(),
            )
        }.await()
        saveHouseholdId(userId, householdId)

        return JoinHouseholdResult.Joined
    }

    override suspend fun reissueInviteCode(
        ownerUserId: String,
        currentInviteCode: String,
    ): String {
        val normalizedCurrentCode = currentInviteCode.trim().uppercase(Locale.ROOT)
        repeat(MAX_INVITE_CODE_GENERATION_ATTEMPTS) {
            val newInviteCode = generateInviteCode()
            val currentInviteDocument = firestore
                .collection(INVITE_CODES_COLLECTION)
                .document(normalizedCurrentCode)
            val newInviteDocument = firestore
                .collection(INVITE_CODES_COLLECTION)
                .document(newInviteCode)
            val expiresAt = Timestamp(
                (System.currentTimeMillis() / MILLIS_PER_SECOND) + INVITE_TTL_SECONDS,
                0,
            )

            val result = runCatching {
                firestore.runTransaction { transaction ->
                    val currentInviteSnapshot = transaction.get(currentInviteDocument)
                    check(currentInviteSnapshot.exists()) { "Current invite code does not exist." }
                    check(currentInviteSnapshot.getString(CREATED_BY_FIELD) == ownerUserId) {
                        "Only the household owner can reissue its invite code."
                    }
                    val householdId = checkNotNull(
                        currentInviteSnapshot.getString(HOUSEHOLD_ID_FIELD),
                    ) { "Invite code is missing household id." }
                    val householdDocument = firestore
                        .collection(HOUSEHOLDS_COLLECTION)
                        .document(householdId)
                    val householdSnapshot = transaction.get(householdDocument)
                    check(householdSnapshot.getString(OWNER_ID_FIELD) == ownerUserId) {
                        "Only the household owner can reissue its invite code."
                    }
                    check(!transaction.get(newInviteDocument).exists()) {
                        INVITE_CODE_COLLISION_MESSAGE
                    }

                    transaction.update(
                        householdDocument,
                        mapOf(
                            INVITE_CODE_FIELD to newInviteCode,
                            UPDATED_AT_FIELD to FieldValue.serverTimestamp(),
                        ),
                    )
                    transaction.set(
                        newInviteDocument,
                        mapOf(
                            CODE_FIELD to newInviteCode,
                            HOUSEHOLD_ID_FIELD to householdId,
                            CREATED_BY_FIELD to ownerUserId,
                            EXPIRES_AT_FIELD to expiresAt,
                            CREATED_AT_FIELD to FieldValue.serverTimestamp(),
                        ),
                    )
                    newInviteCode
                }.await()
            }
            result.getOrNull()?.let { return it }
            val error = result.exceptionOrNull()
            if (error?.message != INVITE_CODE_COLLISION_MESSAGE) {
                throw checkNotNull(error)
            }
        }
        error("Failed to generate a unique invite code.")
    }

    override suspend fun getLatestInviteCode(ownerUserId: String): String? {
        return firestore
            .collection(INVITE_CODES_COLLECTION)
            .whereEqualTo(CREATED_BY_FIELD, ownerUserId)
            .get()
            .await()
            .documents
            .maxByOrNull { document ->
                document.getTimestamp(EXPIRES_AT_FIELD)?.seconds ?: Long.MIN_VALUE
            }
            ?.getString(CODE_FIELD)
    }

    override suspend fun hasJoinedHousehold(userId: String): Boolean {
        val userDocument = firestore.collection(USERS_COLLECTION).document(userId)
        val savedHouseholdId = userDocument.get().await().getString(HOUSEHOLD_ID_FIELD)
        if (!savedHouseholdId.isNullOrBlank()) {
            return true
        }

        val ownedHouseholdId = runCatching {
            firestore
                .collection(HOUSEHOLDS_COLLECTION)
                .whereEqualTo(OWNER_ID_FIELD, userId)
                .limit(1)
                .get()
                .await()
                .documents
                .firstOrNull()
                ?.id
        }.getOrNull()
        if (ownedHouseholdId != null) {
            saveHouseholdId(userId, ownedHouseholdId)
            return true
        }

        val createdHouseholdId = firestore
            .collection(INVITE_CODES_COLLECTION)
            .whereEqualTo(CREATED_BY_FIELD, userId)
            .limit(1)
            .get()
            .await()
            .documents
            .firstOrNull()
            ?.getString(HOUSEHOLD_ID_FIELD)
        if (!createdHouseholdId.isNullOrBlank()) {
            saveHouseholdId(userId, createdHouseholdId)
            return true
        }

        val existingMembership = firestore
            .collectionGroup(MEMBERS_COLLECTION)
            .whereEqualTo(ID_FIELD, userId)
            .limit(1)
            .get()
            .await()
            .documents
            .firstOrNull()
            ?: return false
        val householdId = existingMembership.getString(HOUSEHOLD_ID_FIELD)
            ?: existingMembership.reference.parent.parent?.id
            ?: return false

        saveHouseholdId(userId, householdId)
        return true
    }

    override suspend fun getHouseholdMembers(userId: String): List<HouseholdMember> {
        val householdId = findHouseholdId(userId)
            ?: throw IllegalStateException("User does not belong to a household.")
        val memberSnapshots = firestore
            .collection(HOUSEHOLDS_COLLECTION)
            .document(householdId)
            .collection(MEMBERS_COLLECTION)
            .get()
            .await()
            .documents

        return memberSnapshots.map { memberSnapshot ->
            val memberId = memberSnapshot.getString(ID_FIELD) ?: memberSnapshot.id
            val userSnapshot = firestore
                .collection(USERS_COLLECTION)
                .document(memberId)
                .get()
                .await()
            HouseholdMember(
                userId = memberId,
                displayName = userSnapshot.getString(DISPLAY_NAME_FIELD)
                    ?.trim()
                    ?.takeIf(String::isNotEmpty)
                    ?: userSnapshot.getString(EMAIL_FIELD)
                        ?.trim()
                        ?.takeIf(String::isNotEmpty),
            )
        }
    }

    private suspend fun findHouseholdId(userId: String): String? {
        val savedHouseholdId = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .get()
            .await()
            .getString(HOUSEHOLD_ID_FIELD)
        if (!savedHouseholdId.isNullOrBlank()) return savedHouseholdId

        val membership = firestore
            .collectionGroup(MEMBERS_COLLECTION)
            .whereEqualTo(ID_FIELD, userId)
            .limit(1)
            .get()
            .await()
            .documents
            .firstOrNull()

        return membership?.getString(HOUSEHOLD_ID_FIELD)
            ?: membership?.reference?.parent?.parent?.id
    }

    /**
     * Membership documents remain the source of truth. This denormalized reference only speeds up
     * app startup, so a rules deployment delay must not make household creation or restoration fail.
     */
    private suspend fun saveHouseholdId(userId: String, householdId: String) {
        runCatching {
            firestore.collection(USERS_COLLECTION).document(userId).set(
                mapOf(
                    HOUSEHOLD_ID_FIELD to householdId,
                    UPDATED_AT_FIELD to FieldValue.serverTimestamp(),
                ),
                SetOptions.merge(),
            ).await()
        }
    }

    private fun generateInviteCode(): String {
        val suffix = (1..INVITE_CODE_SUFFIX_LENGTH)
            .map { INVITE_CODE_CHARS.random(Random.Default) }
            .joinToString(separator = "")
        return "$INVITE_CODE_PREFIX-$suffix"
    }

    private companion object {
        const val HOUSEHOLDS_COLLECTION = "households"
        const val USERS_COLLECTION = "users"
        const val MEMBERS_COLLECTION = "members"
        const val INVITE_CODES_COLLECTION = "inviteCodes"
        const val ID_FIELD = "id"
        const val NAME_FIELD = "name"
        const val DISPLAY_NAME_FIELD = "displayName"
        const val EMAIL_FIELD = "email"
        const val OWNER_ID_FIELD = "ownerId"
        const val INVITE_CODE_FIELD = "inviteCode"
        const val HOUSEHOLD_ID_FIELD = "householdId"
        const val ROLE_FIELD = "role"
        const val JOINED_WITH_INVITE_CODE_FIELD = "joinedWithInviteCode"
        const val CODE_FIELD = "code"
        const val CREATED_BY_FIELD = "createdBy"
        const val EXPIRES_AT_FIELD = "expiresAt"
        const val CREATED_AT_FIELD = "createdAt"
        const val UPDATED_AT_FIELD = "updatedAt"
        const val OWNER_ROLE = "owner"
        const val MEMBER_ROLE = "member"
        const val INVITE_CODE_PREFIX = "MZ"
        const val INVITE_CODE_SUFFIX_LENGTH = 4
        const val MAX_INVITE_CODE_GENERATION_ATTEMPTS = 5
        const val INVITE_CODE_COLLISION_MESSAGE = "Invite code collision."
        const val INVITE_TTL_SECONDS = 24 * 60 * 60
        const val MILLIS_PER_SECOND = 1_000
        const val INVITE_CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
    }
}
