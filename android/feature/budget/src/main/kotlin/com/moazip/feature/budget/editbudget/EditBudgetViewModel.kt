package com.moazip.feature.budget.editbudget

import androidx.lifecycle.viewModelScope
import com.moazip.core.domain.auth.CurrentUserProvider
import com.moazip.core.domain.repository.BudgetRepository
import com.moazip.core.presentation.mvi.MviViewModel
import com.moazip.feature.budget.contract.BudgetAllocationUiModel
import com.moazip.feature.budget.contract.MonthlyBudgetState
import com.moazip.feature.budget.contract.BudgetError
import com.moazip.feature.budget.defaultBudgetState
import com.moazip.feature.budget.editbudget.contract.EditBudgetEffect
import com.moazip.feature.budget.editbudget.contract.EditBudgetIntent
import com.moazip.feature.budget.editbudget.contract.BudgetItemSection
import com.moazip.feature.budget.toModel
import com.moazip.feature.budget.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.YearMonth
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class EditBudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val currentUserProvider: CurrentUserProvider,
) : MviViewModel<EditBudgetIntent, MonthlyBudgetState, EditBudgetEffect>(defaultBudgetState().copy(isLoading = true)) {
    init { load() }

    override fun onIntent(intent: EditBudgetIntent) {
        val memberIndex = when (intent) {
            is EditBudgetIntent.ChangeIncome -> intent.memberIndex
            is EditBudgetIntent.ChangeAllocationName -> intent.memberIndex
            is EditBudgetIntent.ChangeAllocationAmount -> intent.memberIndex
            is EditBudgetIntent.AddAllocation -> intent.memberIndex
            is EditBudgetIntent.RemoveAllocation -> intent.memberIndex
            EditBudgetIntent.SaveClicked, EditBudgetIntent.CancelClicked -> null
        }
        when (intent) {
            is EditBudgetIntent.ChangeIncome -> reduce { copy(members = members.mapIndexed { i, member ->
                if (i == intent.memberIndex) member.copy(income = intent.value.digitsToLong()) else member
            }) }
            is EditBudgetIntent.ChangeAllocationName -> updateAllocation(memberIndex, intent.section, intent.itemIndex) { copy(name = intent.value) }
            is EditBudgetIntent.ChangeAllocationAmount -> updateAllocation(memberIndex, intent.section, intent.itemIndex) { copy(amount = intent.value.digitsToLong()) }
            is EditBudgetIntent.AddAllocation -> reduce {
                if (memberIndex == null) when (intent.section) {
                    BudgetItemSection.BUDGET -> copy(jointAllocations = jointAllocations + BudgetAllocationUiModel("", 0))
                    BudgetItemSection.SAVING -> copy(jointSavings = jointSavings + BudgetAllocationUiModel("", 0))
                } else copy(members = members.mapIndexed { i, member ->
                    if (i != memberIndex) member else when (intent.section) {
                        BudgetItemSection.BUDGET -> member.copy(budgetAllocations = member.budgetAllocations + BudgetAllocationUiModel("", 0))
                        BudgetItemSection.SAVING -> member.copy(savings = member.savings + BudgetAllocationUiModel("", 0))
                    }
                })
            }
            is EditBudgetIntent.RemoveAllocation -> reduce {
                if (memberIndex == null) when (intent.section) {
                    BudgetItemSection.BUDGET -> copy(jointAllocations = jointAllocations.filterIndexed { i, _ -> i != intent.itemIndex })
                    BudgetItemSection.SAVING -> copy(jointSavings = jointSavings.filterIndexed { i, _ -> i != intent.itemIndex })
                } else copy(members = members.mapIndexed { i, member ->
                    if (i != memberIndex) member else when (intent.section) {
                        BudgetItemSection.BUDGET -> member.copy(budgetAllocations = member.budgetAllocations.filterIndexed { j, _ -> j != intent.itemIndex })
                        BudgetItemSection.SAVING -> member.copy(savings = member.savings.filterIndexed { j, _ -> j != intent.itemIndex })
                    }
                })
            }
            EditBudgetIntent.SaveClicked -> save()
            EditBudgetIntent.CancelClicked -> postEffect(EditBudgetEffect.NavigateBack)
        }
    }

    private fun load() {
        val userId = currentUserProvider.userId ?: return reduce { copy(isLoading = false, error = BudgetError.UNAUTHENTICATED) }
        viewModelScope.launch {
            val currentMonthId = YearMonth.now().toString()
            val previousMonthId = YearMonth.now().minusMonths(1).toString()
            runCatching {
                budgetRepository.observeMonthlyBudget(userId, currentMonthId).first()
                    ?.toUiState()
                    ?: budgetRepository.observeBudgetHistory(userId).first()
                        .firstOrNull { it.monthId == previousMonthId }
                        ?.toUiState(currentMonthId, isCopiedFromPreviousMonth = true)
                    ?: defaultBudgetState()
            }
                .onSuccess { loadedState -> reduce { loadedState.copy(isLoading = false) } }
                .onFailure { reduce { copy(isLoading = false, error = BudgetError.LOAD_FAILED) } }
        }
    }

    private fun save() {
        val userId = currentUserProvider.userId ?: return reduce { copy(error = BudgetError.UNAUTHENTICATED) }
        val plan = state.value.toModel()
        viewModelScope.launch {
            reduce { copy(isSaving = true, error = null) }
            runCatching { budgetRepository.saveMonthlyBudget(userId, plan) }
                .onSuccess { reduce { copy(isSaving = false) }; postEffect(EditBudgetEffect.Saved) }
                .onFailure { reduce { copy(isSaving = false, error = BudgetError.SAVE_FAILED) } }
        }
    }

    private fun updateAllocation(memberIndex: Int?, section: BudgetItemSection, itemIndex: Int, transform: BudgetAllocationUiModel.() -> BudgetAllocationUiModel) = reduce {
        if (memberIndex == null) when (section) {
            BudgetItemSection.BUDGET -> copy(jointAllocations = jointAllocations.mapIndexed { i, item -> if (i == itemIndex) item.transform() else item })
            BudgetItemSection.SAVING -> copy(jointSavings = jointSavings.mapIndexed { i, item -> if (i == itemIndex) item.transform() else item })
        } else copy(members = members.mapIndexed { i, member ->
            if (i != memberIndex) member else when (section) {
                BudgetItemSection.BUDGET -> member.copy(budgetAllocations = member.budgetAllocations.mapIndexed { j, item -> if (j == itemIndex) item.transform() else item })
                BudgetItemSection.SAVING -> member.copy(savings = member.savings.mapIndexed { j, item -> if (j == itemIndex) item.transform() else item })
            }
        })
    }

    private fun String.digitsToLong() = filter(Char::isDigit).toLongOrNull() ?: 0L
}
