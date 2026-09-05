package com.moazip.feature.budget.route

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moazip.feature.budget.contract.BudgetAllocationUiModel
import com.moazip.feature.budget.contract.MemberBudgetUiModel
import com.moazip.feature.budget.contract.MonthlyBudgetState
import com.moazip.feature.budget.ui.MonthlyBudgetScreen

@Composable
fun MonthlyBudgetRoute(
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MonthlyBudgetScreen(
        state = MonthlyBudgetState(
            monthLabel = "2026년 9월",
            members = listOf(
                MemberBudgetUiModel(
                    name = "재웅",
                    income = 3_500_000,
                    allocations = listOf(
                        BudgetAllocationUiModel("생활비 · 용돈", 600_000),
                        BudgetAllocationUiModel("교통비 · 식비", 250_000),
                        BudgetAllocationUiModel("ISA", 300_000),
                        BudgetAllocationUiModel("해외주식", 500_000),
                    ),
                ),
                MemberBudgetUiModel(
                    name = "은정",
                    income = 4_000_000,
                    allocations = listOf(
                        BudgetAllocationUiModel("월세 · 관리비", 800_000),
                        BudgetAllocationUiModel("용돈 · 교통비 · 식비", 350_000),
                        BudgetAllocationUiModel("ISA", 300_000),
                        BudgetAllocationUiModel("해외주식", 1_000_000),
                    ),
                ),
            ),
            jointSavings = listOf(
                BudgetAllocationUiModel("경조사비", 200_000),
                BudgetAllocationUiModel("비정기 지출 적립", 300_000),
            ),
        ),
        onEditClick = onEditClick,
        modifier = modifier,
    )
}
