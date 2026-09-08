package com.moazip.feature.budget.history

import androidx.lifecycle.viewModelScope
import com.moazip.core.domain.auth.CurrentUserProvider
import com.moazip.core.domain.repository.BudgetRepository
import com.moazip.core.presentation.mvi.MviViewModel
import com.moazip.feature.budget.contract.BudgetError
import com.moazip.feature.budget.history.contract.BudgetHistoryEffect
import com.moazip.feature.budget.history.contract.BudgetHistoryIntent
import com.moazip.feature.budget.history.contract.BudgetHistoryState
import com.moazip.feature.budget.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@HiltViewModel
class BudgetHistoryViewModel @Inject constructor(
    budgetRepository: BudgetRepository,
    currentUserProvider: CurrentUserProvider,
) : MviViewModel<BudgetHistoryIntent, BudgetHistoryState, BudgetHistoryEffect>(BudgetHistoryState()) {
    init {
        val userId = currentUserProvider.userId
        if (userId == null) reduce { copy(isLoading = false, error = BudgetError.UNAUTHENTICATED) }
        else viewModelScope.launch {
            budgetRepository.observeBudgetHistory(userId)
                .onStart { reduce { copy(isLoading = true, error = null) } }
                .catch { reduce { copy(isLoading = false, error = BudgetError.LOAD_FAILED) } }
                .collect { plans -> reduce { copy(plans = plans.map { it.toUiState() }, isLoading = false, error = null) } }
        }
    }

    override fun onIntent(intent: BudgetHistoryIntent) = Unit
}
