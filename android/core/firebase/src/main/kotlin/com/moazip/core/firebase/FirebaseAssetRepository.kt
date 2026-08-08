package com.moazip.core.firebase

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.moazip.core.domain.repository.AssetRepository
import com.moazip.core.model.Asset
import com.moazip.core.model.AssetCategory
import com.moazip.core.model.AssetKind
import com.moazip.core.model.AssetStatus
import com.moazip.core.model.NewAsset
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseAssetRepository(
    private val firestore: FirebaseFirestore,
) : AssetRepository {
    override fun observeAssets(userId: String): Flow<List<Asset>> = flow {
        val householdId = findHouseholdId(userId)
            ?: throw IllegalStateException("User does not belong to a household.")
        emitAll(observeHouseholdAssets(householdId))
    }

    override suspend fun getAsset(userId: String, assetId: String): Asset {
        val householdId = findHouseholdId(userId)
            ?: throw IllegalStateException("User does not belong to a household.")
        return firestore.collection(HOUSEHOLDS_COLLECTION).document(householdId)
            .collection(ASSETS_COLLECTION).document(assetId).get().await()
            .toAsset(householdId)
            ?: throw NoSuchElementException("Asset not found.")
    }

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

    override suspend fun updateAsset(userId: String, assetId: String, asset: NewAsset) {
        val householdId = findHouseholdId(userId)
            ?: throw IllegalStateException("User does not belong to a household.")
        firestore.collection(HOUSEHOLDS_COLLECTION).document(householdId)
            .collection(ASSETS_COLLECTION).document(assetId)
            .update(
                mapOf(
                    NAME_FIELD to asset.name.trim(),
                    OWNER_TYPE_FIELD to if (asset.ownerUserId == null) COMMON_OWNER else MEMBER_OWNER,
                    OWNER_ID_FIELD to asset.ownerUserId,
                    OWNER_NAME_FIELD to asset.ownerDisplayName,
                    KIND_FIELD to asset.kind.name,
                    CATEGORY_FIELD to asset.category.name,
                    CURRENT_AMOUNT_FIELD to asset.currentAmount,
                    MEMO_FIELD to asset.memo.trim(),
                    UPDATED_AT_FIELD to FieldValue.serverTimestamp(),
                ),
            ).await()
    }

    override suspend fun deleteAsset(userId: String, assetId: String) {
        val householdId = findHouseholdId(userId)
            ?: throw IllegalStateException("User does not belong to a household.")
        firestore.collection(HOUSEHOLDS_COLLECTION).document(householdId)
            .collection(ASSETS_COLLECTION).document(assetId)
            .delete()
            .await()
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

    private fun observeHouseholdAssets(householdId: String): Flow<List<Asset>> = callbackFlow {
        val registration = firestore
            .collection(HOUSEHOLDS_COLLECTION)
            .document(householdId)
            .collection(ASSETS_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }

                val assets = snapshot
                    ?.documents
                    .orEmpty()
                    .mapNotNull { document -> document.toAsset(householdId) }
                    .sortedByDescending(Asset::updatedAtMillis)
                trySend(assets)
            }

        awaitClose(registration::remove)
    }

    private fun DocumentSnapshot.toAsset(householdId: String): Asset? {
        val kind = getString(KIND_FIELD)?.toEnumOrNull<AssetKind>() ?: return null
        val category = getString(CATEGORY_FIELD)?.toEnumOrNull<AssetCategory>() ?: return null
        val status = getString(STATUS_FIELD)?.toEnumOrNull<AssetStatus>() ?: AssetStatus.ACTIVE
        return Asset(
            id = getString(ID_FIELD) ?: id,
            householdId = getString(HOUSEHOLD_ID_FIELD) ?: householdId,
            ownerId = getString(OWNER_ID_FIELD),
            ownerName = getString(OWNER_NAME_FIELD),
            kind = kind,
            category = category,
            name = getString(NAME_FIELD).orEmpty(),
            currentAmount = getLong(CURRENT_AMOUNT_FIELD) ?: 0L,
            principal = getLong(PRINCIPAL_FIELD),
            profit = getLong(PROFIT_FIELD),
            returnRate = getDouble(RETURN_RATE_FIELD),
            memo = getString(MEMO_FIELD).orEmpty(),
            status = status,
            recordedAtMillis = getTimestamp(RECORDED_AT_FIELD)?.toDate()?.time,
            updatedAtMillis = getTimestamp(UPDATED_AT_FIELD)?.toDate()?.time ?: 0L,
        )
    }

    private inline fun <reified T : Enum<T>> String.toEnumOrNull(): T? =
        enumValues<T>().firstOrNull { it.name == this }

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
        const val PRINCIPAL_FIELD = "principal"
        const val PROFIT_FIELD = "profit"
        const val RETURN_RATE_FIELD = "returnRate"
        const val MEMO_FIELD = "memo"
        const val STATUS_FIELD = "status"
        const val RECORDED_AT_FIELD = "recordedAt"
        const val CREATED_BY_FIELD = "createdBy"
        const val CREATED_AT_FIELD = "createdAt"
        const val UPDATED_AT_FIELD = "updatedAt"
        const val COMMON_OWNER = "COMMON"
        const val MEMBER_OWNER = "MEMBER"
        const val ACTIVE_STATUS = "ACTIVE"
    }
}
