package com.moazip.feature.budget

import androidx.lifecycle.viewModelScope
import com.moazip.core.domain.auth.CurrentUserProvider
import com.moazip.core.domain.repository.BudgetRepository
import com.moazip.core.presentation.mvi.MviViewModel
import com.moazip.feature.budget.contract.MonthlyBudgetEffect
import com.moazip.feature.budget.contract.MonthlyBudgetIntent
import com.moazip.feature.budget.contract.MonthlyBudgetState
import com.moazip.feature.budget.contract.BudgetError
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.YearMonth
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class MonthlyBudgetViewModel @Inject constructor(
    budgetRepository: BudgetRepository,
    currentUserProvider: CurrentUserProvider,
) : MviViewModel<MonthlyBudgetIntent, MonthlyBudgetState, MonthlyBudgetEffect>(defaultBudgetState()) {
    init {
        val userId = currentUserProvider.userId
        if (userId == null) reduce { copy(error = BudgetError.UNAUTHENTICATED) }
        else viewModelScope.launch {
            val currentMonthId = YearMonth.now().toString()
            val previousMonthId = YearMonth.now().minusMonths(1).toString()
            budgetRepository.observeMonthlyBudget(userId, currentMonthId)
                .onStart { reduce { copy(isLoading = true, error = null) } }
                .catch { reduce { copy(isLoading = false, error = BudgetError.LOAD_FAILED) } }
                .collect { plan ->
                    val state = plan?.toUiState() ?: budgetRepository.observeBudgetHistory(userId).first()
                        .firstOrNull { it.monthId == previousMonthId }
                        ?.toUiState(currentMonthId, isCopiedFromPreviousMonth = true)
                        ?: defaultBudgetState()
                    reduce { state.copy(isLoading = false) }
                }
        }
    }

    override fun onIntent(intent: MonthlyBudgetIntent) = Unit
}
