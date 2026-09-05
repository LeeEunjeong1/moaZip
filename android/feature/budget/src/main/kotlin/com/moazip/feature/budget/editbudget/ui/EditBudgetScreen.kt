package com.moazip.feature.budget.editbudget.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipButton
import com.moazip.core.ui.component.MoaZipOutlinedButton
import com.moazip.core.ui.component.MoaZipTextField
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.budget.contract.BudgetAllocationUiModel
import com.moazip.feature.budget.contract.MonthlyBudgetState
import com.moazip.feature.budget.editbudget.contract.EditBudgetIntent
import java.text.NumberFormat
import java.util.Locale

@Composable
fun EditBudgetScreen(
    state: MonthlyBudgetState,
    onIntent: (EditBudgetIntent) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(MoaZipPalette.Cream50).imePadding()) {
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 24.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            item {
                TextButton(onClick = onBack, contentPadding = PaddingValues(0.dp)) { Text("‹  돌아가기") }
                Text("예산 계획 수정", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("${state.monthLabel} 배분 금액을 수정해요.", color = MoaZipPalette.Gray500)
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("예상 저축 가능액", color = MoaZipPalette.Gray500)
                    Text(state.availableSaving.asWon(), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("월급 ${state.totalIncome.asWon()} · 배분 ${state.totalAllocation.asWon()}")
                }
            }
            state.errorMessage?.let { message ->
                item { Text(message, color = MaterialTheme.colorScheme.error) }
            }
            itemsIndexed(state.members, key = { _, member -> member.name }) { memberIndex, member ->
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("${member.name} 예산", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("월급", fontWeight = FontWeight.SemiBold)
                    AmountField(member.income) { onIntent(EditBudgetIntent.ChangeIncome(memberIndex, it)) }
                    member.allocations.forEachIndexed { itemIndex, item ->
                        AllocationEditor(item, {
                            onIntent(EditBudgetIntent.ChangeAllocationName(memberIndex, itemIndex, it))
                        }, {
                            onIntent(EditBudgetIntent.ChangeAllocationAmount(memberIndex, itemIndex, it))
                        }, {
                            onIntent(EditBudgetIntent.RemoveAllocation(memberIndex, itemIndex))
                        })
                    }
                    TextButton(onClick = { onIntent(EditBudgetIntent.AddAllocation(memberIndex)) }) { Text("+ 항목 추가") }
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("공동 적립", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    state.jointSavings.forEachIndexed { itemIndex, item ->
                        AllocationEditor(item, {
                            onIntent(EditBudgetIntent.ChangeAllocationName(null, itemIndex, it))
                        }, {
                            onIntent(EditBudgetIntent.ChangeAllocationAmount(null, itemIndex, it))
                        }, {
                            onIntent(EditBudgetIntent.RemoveAllocation(null, itemIndex))
                        })
                    }
                    TextButton(onClick = { onIntent(EditBudgetIntent.AddAllocation(null)) }) { Text("+ 공동 항목 추가") }
                }
            }
        }
        Row(
            Modifier.fillMaxWidth().background(MoaZipPalette.Cream50).padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            MoaZipOutlinedButton("취소", onBack, Modifier.weight(1f))
            MoaZipButton(
                text = if (state.isSaving) "저장 중..." else "저장하기",
                onClick = onSave,
                modifier = Modifier.weight(1f),
                enabled = !state.isSaving,
            )
        }
    }
}

@Composable
private fun AllocationEditor(
    item: BudgetAllocationUiModel,
    onNameChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onRemove: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Text("배분 항목", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            TextButton(onClick = onRemove) { Text("삭제", color = MaterialTheme.colorScheme.error) }
        }
        MoaZipTextField(item.name, onNameChange, placeholder = "예: ISA")
        AmountField(item.amount, onAmountChange)
    }
}

@Composable
private fun AmountField(amount: Long, onValueChange: (String) -> Unit) {
    MoaZipTextField(
        value = if (amount == 0L) "" else NumberFormat.getNumberInstance(Locale.KOREA).format(amount),
        onValueChange = onValueChange,
        placeholder = "금액 입력",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}

private fun Long.asWon(): String = "${NumberFormat.getNumberInstance(Locale.KOREA).format(this)}원"
