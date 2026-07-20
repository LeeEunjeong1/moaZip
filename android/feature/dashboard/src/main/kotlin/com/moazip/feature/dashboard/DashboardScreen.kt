package com.moazip.feature.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DashboardRoute(
    viewModel: DashboardViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    DashboardScreen(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
fun DashboardScreen(
    state: DashboardState,
    onIntent: (DashboardIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isLoading) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("우리 집 자산", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("순자산 ${state.summary.netWorth.asWon()}", style = MaterialTheme.typography.headlineSmall)
        Text("지난 분기보다 ${state.summary.quarterlyGrowthRate}% 성장했어요")

        SummaryCard("금융 자산", state.summary.financialAssets.asWon())
        SummaryCard("투자 손익", state.summary.investmentProfitLoss.asWon())
        SummaryCard("월 저축액", state.summary.monthlySavings.asWon())

        Button(onClick = { onIntent(DashboardIntent.OpenAssets) }) {
            Text("자산 목록 보기")
        }
    }
}

@Composable
private fun SummaryCard(label: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(label)
            Text(value, fontWeight = FontWeight.SemiBold)
        }
    }
}

private fun Long.asWon(): String = NumberFormat.getCurrencyInstance(Locale.KOREA).format(this)
