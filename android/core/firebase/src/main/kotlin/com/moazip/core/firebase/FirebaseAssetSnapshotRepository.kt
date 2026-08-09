package com.moazip.core.firebase

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.moazip.core.domain.repository.AssetSnapshotRepository
import com.moazip.core.model.AssetSnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseAssetSnapshotRepository(
    private val firestore: FirebaseFirestore,
) : AssetSnapshotRepository {
    override fun observeSnapshots(userId: String): Flow<List<AssetSnapshot>> = flow {
        val householdId = findHouseholdId(userId)
            ?: throw IllegalStateException("User does not belong to a household.")
        emitAll(callbackFlow {
            val registration = firestore.collection("households").document(householdId)
                .collection("assetSnapshots")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    trySend(snapshot?.documents.orEmpty().mapNotNull { document ->
                        val monthKey = document.getString("monthKey") ?: return@mapNotNull null
                        AssetSnapshot(
                            id = document.id,
                            monthKey = monthKey,
                            assetTotal = document.getLong("assetTotal") ?: 0L,
                            investmentTotal = document.getLong("investmentTotal") ?: 0L,
                            liabilityTotal = document.getLong("liabilityTotal") ?: 0L,
                            financialAssetTotal = document.getLong("financialAssetTotal")
                                ?: ((document.getLong("assetTotal") ?: 0L) +
                                    (document.getLong("investmentTotal") ?: 0L)),
                            depositTotal = document.getLong("depositTotal") ?: 0L,
                            recordedAtMillis = document.getTimestamp("recordedAt")?.toDate()?.time ?: 0L,
                        )
                    }.sortedBy { it.monthKey })
                }
            awaitClose(registration::remove)
        })
    }

    override suspend fun saveMonthlySnapshot(userId: String, snapshot: AssetSnapshot) {
        val householdId = findHouseholdId(userId)
            ?: throw IllegalStateException("User does not belong to a household.")
        firestore.collection("households").document(householdId)
            .collection("assetSnapshots").document(snapshot.monthKey)
            .set(
                mapOf(
                    "id" to snapshot.monthKey,
                    "householdId" to householdId,
                    "monthKey" to snapshot.monthKey,
                    "assetTotal" to snapshot.assetTotal,
                    "investmentTotal" to snapshot.investmentTotal,
                    "liabilityTotal" to snapshot.liabilityTotal,
                    "financialAssetTotal" to snapshot.financialAssetTotal,
                    "depositTotal" to snapshot.depositTotal,
                    "netWorth" to snapshot.netWorth,
                    "recordedBy" to userId,
                    "recordedAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp(),
                ),
            ).await()
    }

    private suspend fun findHouseholdId(userId: String): String? {
        val householdId = firestore.collection("users").document(userId).get().await()
            .getString("householdId")
        if (!householdId.isNullOrBlank()) return householdId
        val membership = firestore.collectionGroup("members")
            .whereEqualTo("id", userId).limit(1).get().await().documents.firstOrNull()
        return membership?.getString("householdId") ?: membership?.reference?.parent?.parent?.id
    }
}
