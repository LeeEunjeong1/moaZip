package com.moazip.feature.budget.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.moazip.feature.budget.R
import java.time.YearMonth

@Composable
internal fun formatBudgetMonth(monthId: String): String {
    val month = runCatching { YearMonth.parse(monthId) }.getOrDefault(YearMonth.now())
    return stringResource(R.string.budget_month_format, month.year, month.monthValue)
}
