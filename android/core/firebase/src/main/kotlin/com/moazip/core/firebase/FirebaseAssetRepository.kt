package com.moazip.core.firebase

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.moazip.core.domain.repository.AssetRepository
import com.moazip.core.model.NewAsset
import kotlinx.coroutines.tasks.await

class FirebaseAssetRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : AssetRepository {
    override suspend fun addAsset(
        userId: String,
        asset: NewAsset,
    ): String {
        val householdId = findHouseholdId(userId)
            ?: throw IllegalStateException("User does not belong to a household.")
        val assetDocument = firestore
            .collection(HOUSEHOLDS_COLLECTION)
            .document(householdId)
            .collection(ASSETS_COLLECTION)
            .document()

        assetDocument.set(
            mapOf(
                ID_FIELD to assetDocument.id,
                HOUSEHOLD_ID_FIELD to householdId,
                NAME_FIELD to asset.name.trim(),
                OWNER_TYPE_FIELD to if (asset.ownerUserId == null) COMMON_OWNER else MEMBER_OWNER,
                OWNER_ID_FIELD to asset.ownerUserId,
                OWNER_NAME_FIELD to asset.ownerDisplayName,
                KIND_FIELD to asset.kind.name,
                CATEGORY_FIELD to asset.category.name,
                CURRENT_AMOUNT_FIELD to asset.currentAmount,
                MEMO_FIELD to asset.memo.trim(),
                STATUS_FIELD to ACTIVE_STATUS,
                CREATED_BY_FIELD to userId,
                CREATED_AT_FIELD to FieldValue.serverTimestamp(),
                UPDATED_AT_FIELD to FieldValue.serverTimestamp(),
            ),
        ).await()

        return assetDocument.id
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

    private companion object {
        const val USERS_COLLECTION = "users"
        const val HOUSEHOLDS_COLLECTION = "households"
        const val MEMBERS_COLLECTION = "members"
        const val ASSETS_COLLECTION = "assets"
        const val ID_FIELD = "id"
        const val HOUSEHOLD_ID_FIELD = "householdId"
        const val NAME_FIELD = "name"
        const val OWNER_TYPE_FIELD = "ownerType"
        const val OWNER_ID_FIELD = "ownerId"
        const val OWNER_NAME_FIELD = "ownerName"
        const val KIND_FIELD = "kind"
        const val CATEGORY_FIELD = "category"
        const val CURRENT_AMOUNT_FIELD = "currentAmount"
        const val MEMO_FIELD = "memo"
        const val STATUS_FIELD = "status"
        const val CREATED_BY_FIELD = "createdBy"
        const val CREATED_AT_FIELD = "createdAt"
        const val UPDATED_AT_FIELD = "updatedAt"
        const val COMMON_OWNER = "COMMON"
        const val MEMBER_OWNER = "MEMBER"
        const val ACTIVE_STATUS = "ACTIVE"
    }
}
