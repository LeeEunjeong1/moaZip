package com.moazip.core.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.moazip.core.domain.repository.BudgetRepository
import com.moazip.core.model.BudgetAllocation
import com.moazip.core.model.MemberBudget
import com.moazip.core.model.MonthlyBudgetPlan
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseBudgetRepository(private val firestore: FirebaseFirestore) : BudgetRepository {
    override fun observeMonthlyBudget(userId: String, monthId: String): Flow<MonthlyBudgetPlan?> = flow {
        val householdId = findHouseholdId(userId)
            ?: throw IllegalStateException("User does not belong to a household.")
        emitAll(callbackFlow {
            val registration = budgetDocument(householdId, monthId).addSnapshotListener { snapshot, error ->
                if (error != null) close(error) else trySend(snapshot?.takeIf(DocumentSnapshot::exists)?.toPlan(monthId))
            }
            awaitClose(registration::remove)
        })
    }

    override suspend fun saveMonthlyBudget(userId: String, plan: MonthlyBudgetPlan) {
        val householdId = findHouseholdId(userId)
            ?: throw IllegalStateException("User does not belong to a household.")
        budgetDocument(householdId, plan.monthId).set(
            mapOf(
                "monthId" to plan.monthId,
                "members" to plan.members.map { member ->
                    mapOf(
                        "name" to member.name,
                        "income" to member.income,
                        "budgetAllocations" to member.budgetAllocations.map { it.toMap() },
                        "savings" to member.savings.map { it.toMap() },
                    )
                },
                "jointAllocations" to plan.jointAllocations.map { it.toMap() },
                "jointSavings" to plan.jointSavings.map { it.toMap() },
                "updatedBy" to userId,
                "updatedAt" to FieldValue.serverTimestamp(),
            ),
        ).await()
    }

    private fun budgetDocument(householdId: String, monthId: String) = firestore
        .collection("households").document(householdId).collection("budgetPlans").document(monthId)

    private suspend fun findHouseholdId(userId: String): String? {
        val user = firestore.collection("users").document(userId).get().await()
        return user.getString("householdId") ?: firestore.collectionGroup("members")
            .whereEqualTo("id", userId).limit(1).get().await().documents.firstOrNull()
            ?.let { it.getString("householdId") ?: it.reference.parent.parent?.id }
    }

    private fun DocumentSnapshot.toPlan(fallbackMonthId: String) = MonthlyBudgetPlan(
        monthId = getString("monthId") ?: fallbackMonthId,
        members = (get("members") as? List<*>)?.mapNotNull { raw ->
            val map = raw as? Map<*, *> ?: return@mapNotNull null
            val legacyAllocations = (map["allocations"] as? List<*>)?.toAllocations().orEmpty()
            MemberBudget(
                name = map["name"] as? String ?: return@mapNotNull null,
                income = (map["income"] as? Number)?.toLong() ?: 0L,
                budgetAllocations = (map["budgetAllocations"] as? List<*>)?.toAllocations()
                    ?: legacyAllocations.filterNot { it.isSavingItem() },
                savings = (map["savings"] as? List<*>)?.toAllocations()
                    ?: legacyAllocations.filter { it.isSavingItem() },
            )
        }.orEmpty(),
        jointAllocations = (get("jointAllocations") as? List<*>)?.toAllocations()
            ?: (get("jointSavings") as? List<*>)?.toAllocations().orEmpty(),
        jointSavings = if (contains("jointAllocations")) {
            (get("jointSavings") as? List<*>)?.toAllocations().orEmpty()
        } else emptyList(),
    )

    private fun List<*>.toAllocations() = mapNotNull { raw ->
        val map = raw as? Map<*, *> ?: return@mapNotNull null
        BudgetAllocation(map["name"] as? String ?: return@mapNotNull null, (map["amount"] as? Number)?.toLong() ?: 0L)
    }

    private fun BudgetAllocation.toMap() = mapOf("name" to name.trim(), "amount" to amount)

    private fun BudgetAllocation.isSavingItem(): Boolean {
        val keywords = listOf("ISA", "주식", "적금", "예금", "저축", "연금", "청약")
        return keywords.any { name.contains(it, ignoreCase = true) }
    }
}
