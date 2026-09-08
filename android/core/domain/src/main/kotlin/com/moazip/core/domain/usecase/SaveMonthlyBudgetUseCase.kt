package com.moazip.core.domain.usecase

import com.moazip.core.domain.repository.BudgetRepository
import com.moazip.core.model.MonthlyBudgetPlan

class SaveMonthlyBudgetUseCase(private val repository: BudgetRepository) {
    suspend operator fun invoke(userId: String, plan: MonthlyBudgetPlan) = repository.saveMonthlyBudget(userId, plan)
}
