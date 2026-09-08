package com.moazip.core.domain.usecase

import com.moazip.core.domain.repository.BudgetRepository

class ObserveMonthlyBudgetUseCase(private val repository: BudgetRepository) {
    operator fun invoke(userId: String, monthId: String) = repository.observeMonthlyBudget(userId, monthId)
}
