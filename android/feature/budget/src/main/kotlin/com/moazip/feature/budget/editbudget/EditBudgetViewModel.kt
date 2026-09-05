package com.moazip.feature.budget.editbudget

import androidx.lifecycle.viewModelScope
import com.moazip.core.domain.auth.CurrentUserProvider
import com.moazip.core.domain.repository.BudgetRepository
import com.moazip.core.presentation.mvi.MviViewModel
import com.moazip.feature.budget.contract.BudgetAllocationUiModel
import com.moazip.feature.budget.contract.MonthlyBudgetState
import com.moazip.feature.budget.defaultBudgetState
import com.moazip.feature.budget.editbudget.contract.EditBudgetEffect
import com.moazip.feature.budget.editbudget.contract.EditBudgetIntent
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
            is EditBudgetIntent.ChangeAllocationName -> updateAllocation(memberIndex, intent.itemIndex) { copy(name = intent.value) }
            is EditBudgetIntent.ChangeAllocationAmount -> updateAllocation(memberIndex, intent.itemIndex) { copy(amount = intent.value.digitsToLong()) }
            is EditBudgetIntent.AddAllocation -> reduce {
                if (memberIndex == null) copy(jointSavings = jointSavings + BudgetAllocationUiModel("", 0))
                else copy(members = members.mapIndexed { i, member -> if (i == memberIndex) member.copy(allocations = member.allocations + BudgetAllocationUiModel("", 0)) else member })
            }
            is EditBudgetIntent.RemoveAllocation -> reduce {
                if (memberIndex == null) copy(jointSavings = jointSavings.filterIndexed { i, _ -> i != intent.itemIndex })
                else copy(members = members.mapIndexed { i, member -> if (i == memberIndex) member.copy(allocations = member.allocations.filterIndexed { j, _ -> j != intent.itemIndex }) else member })
            }
            EditBudgetIntent.SaveClicked -> save()
            EditBudgetIntent.CancelClicked -> postEffect(EditBudgetEffect.NavigateBack)
        }
    }

    private fun load() {
        val userId = currentUserProvider.userId ?: return reduce { copy(isLoading = false, errorMessage = "로그인 정보를 확인할 수 없어요.") }
        viewModelScope.launch {
            runCatching { budgetRepository.observeMonthlyBudget(userId, YearMonth.now().toString()).first() }
                .onSuccess { plan -> reduce { (plan?.toUiState() ?: defaultBudgetState()).copy(isLoading = false) } }
                .onFailure { reduce { copy(isLoading = false, errorMessage = "예산을 불러오지 못했어요.") } }
        }
    }

    private fun save() {
        val userId = currentUserProvider.userId ?: return reduce { copy(errorMessage = "로그인 정보를 확인할 수 없어요.") }
        val plan = state.value.toModel()
        viewModelScope.launch {
            reduce { copy(isSaving = true, errorMessage = null) }
            runCatching { budgetRepository.saveMonthlyBudget(userId, plan) }
                .onSuccess { reduce { copy(isSaving = false) }; postEffect(EditBudgetEffect.Saved) }
                .onFailure { reduce { copy(isSaving = false, errorMessage = "예산을 저장하지 못했어요.") } }
        }
    }

    private fun updateAllocation(memberIndex: Int?, itemIndex: Int, transform: BudgetAllocationUiModel.() -> BudgetAllocationUiModel) = reduce {
        if (memberIndex == null) copy(jointSavings = jointSavings.mapIndexed { i, item -> if (i == itemIndex) item.transform() else item })
        else copy(members = members.mapIndexed { i, member -> if (i == memberIndex) member.copy(allocations = member.allocations.mapIndexed { j, item -> if (j == itemIndex) item.transform() else item }) else member })
    }

    private fun String.digitsToLong() = filter(Char::isDigit).toLongOrNull() ?: 0L
}
