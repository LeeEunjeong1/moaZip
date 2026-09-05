package com.moazip.feature.budget.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipButton
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.core.ui.theme.MoaZipTheme
import com.moazip.feature.budget.contract.BudgetAllocationUiModel
import com.moazip.feature.budget.contract.MemberBudgetUiModel
import com.moazip.feature.budget.contract.MonthlyBudgetState
import com.moazip.feature.budget.ui.component.BudgetSummaryCard
import com.moazip.feature.budget.ui.component.JointSavingCard
import com.moazip.feature.budget.ui.component.MemberBudgetCard

@Composable
fun MonthlyBudgetScreen(
    state: MonthlyBudgetState,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().background(MoaZipPalette.Cream50).padding(horizontal = 24.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Text("월급 관리", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(
                "${state.monthLabel} 월급을 어떻게 나눌지 확인해요.",
                style = MaterialTheme.typography.bodyMedium,
                color = MoaZipPalette.Gray500,
            )
        }
        item { BudgetSummaryCard(state) }
        items(state.members, key = MemberBudgetUiModel::name) { MemberBudgetCard(it) }
        item { JointSavingCard(state.jointSavings) }
        item { MoaZipButton(text = "계획 수정하기", onClick = onEditClick) }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 850)
@Composable
private fun MonthlyBudgetScreenPreview() {
    MoaZipTheme {
        MonthlyBudgetScreen(
            MonthlyBudgetState(
                monthLabel = "2026년 9월",
                members = listOf(
                    MemberBudgetUiModel(
                        "재웅",
                        3_500_000,
                        listOf(BudgetAllocationUiModel("생활비 · 용돈", 600_000), BudgetAllocationUiModel("ISA", 300_000)),
                    ),
                    MemberBudgetUiModel(
                        "은정",
                        4_000_000,
                        listOf(BudgetAllocationUiModel("월세 · 관리비", 800_000), BudgetAllocationUiModel("해외주식", 1_000_000)),
                    ),
                ),
                jointSavings = listOf(BudgetAllocationUiModel("경조사비", 200_000)),
            ),
            onEditClick = {},
        )
    }
}
