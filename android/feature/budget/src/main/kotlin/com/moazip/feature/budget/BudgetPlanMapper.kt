package com.moazip.feature.budget

import com.moazip.core.model.BudgetAllocation
import com.moazip.core.model.MemberBudget
import com.moazip.core.model.MonthlyBudgetPlan
import com.moazip.feature.budget.contract.BudgetAllocationUiModel
import com.moazip.feature.budget.contract.MemberBudgetUiModel
import com.moazip.feature.budget.contract.MonthlyBudgetState
import java.time.YearMonth

internal fun MonthlyBudgetPlan.toUiState() = MonthlyBudgetState(
    monthId = monthId,
    monthLabel = monthId,
    members = members.map { member ->
        MemberBudgetUiModel(
            member.name,
            member.income,
            member.budgetAllocations.map { BudgetAllocationUiModel(it.name, it.amount) },
            member.savings.map { BudgetAllocationUiModel(it.name, it.amount) },
        )
    },
    jointAllocations = jointAllocations.map { BudgetAllocationUiModel(it.name, it.amount) },
    jointSavings = jointSavings.map { BudgetAllocationUiModel(it.name, it.amount) },
)

internal fun MonthlyBudgetState.toModel() = MonthlyBudgetPlan(
    monthId = monthId,
    members = members.map { member ->
        MemberBudget(
            member.name,
            member.income,
            member.budgetAllocations.map { BudgetAllocation(it.name, it.amount) },
            member.savings.map { BudgetAllocation(it.name, it.amount) },
        )
    },
    jointAllocations = jointAllocations.map { BudgetAllocation(it.name, it.amount) },
    jointSavings = jointSavings.map { BudgetAllocation(it.name, it.amount) },
)

internal fun defaultBudgetState(month: YearMonth = YearMonth.now()) = MonthlyBudgetState(
    monthId = month.toString(),
    monthLabel = month.toString(),
    members = listOf(
        MemberBudgetUiModel("재웅", 3_500_000,
            listOf(BudgetAllocationUiModel("생활비 · 용돈", 600_000), BudgetAllocationUiModel("교통비 · 식비", 250_000)),
            listOf(BudgetAllocationUiModel("ISA", 300_000), BudgetAllocationUiModel("해외주식", 500_000))),
        MemberBudgetUiModel("은정", 4_000_000,
            listOf(BudgetAllocationUiModel("월세 · 관리비", 800_000), BudgetAllocationUiModel("용돈 · 교통비 · 식비", 350_000)),
            listOf(BudgetAllocationUiModel("ISA", 300_000), BudgetAllocationUiModel("해외주식", 1_000_000))),
    ),
    jointAllocations = listOf(BudgetAllocationUiModel("경조사비", 200_000), BudgetAllocationUiModel("비정기 지출 예산", 300_000)),
    jointSavings = emptyList(),
)
