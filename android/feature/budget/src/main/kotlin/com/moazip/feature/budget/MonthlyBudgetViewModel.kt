package com.moazip.feature.budget

import androidx.lifecycle.viewModelScope
import com.moazip.core.domain.auth.CurrentUserProvider
import com.moazip.core.domain.repository.BudgetRepository
import com.moazip.core.presentation.mvi.MviViewModel
import com.moazip.feature.budget.contract.MonthlyBudgetEffect
import com.moazip.feature.budget.contract.MonthlyBudgetIntent
import com.moazip.feature.budget.contract.MonthlyBudgetState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.YearMonth
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@HiltViewModel
class MonthlyBudgetViewModel @Inject constructor(
    budgetRepository: BudgetRepository,
    currentUserProvider: CurrentUserProvider,
) : MviViewModel<MonthlyBudgetIntent, MonthlyBudgetState, MonthlyBudgetEffect>(defaultBudgetState()) {
    init {
        val userId = currentUserProvider.userId
        if (userId == null) reduce { copy(errorMessage = "로그인 정보를 확인할 수 없어요.") }
        else viewModelScope.launch {
            budgetRepository.observeMonthlyBudget(userId, YearMonth.now().toString())
                .onStart { reduce { copy(isLoading = true, errorMessage = null) } }
                .catch { reduce { copy(isLoading = false, errorMessage = "예산을 불러오지 못했어요.") } }
                .collect { plan -> reduce { (plan?.toUiState() ?: defaultBudgetState()).copy(isLoading = false) } }
        }
    }

    override fun onIntent(intent: MonthlyBudgetIntent) = Unit
}
