package com.moazip.core.domain.repository

import com.moazip.core.model.MonthlyBudgetPlan
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun observeMonthlyBudget(userId: String, monthId: String): Flow<MonthlyBudgetPlan?>
    suspend fun saveMonthlyBudget(userId: String, plan: MonthlyBudgetPlan)
}
