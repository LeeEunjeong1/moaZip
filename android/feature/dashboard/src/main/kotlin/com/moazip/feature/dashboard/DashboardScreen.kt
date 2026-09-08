package com.moazip.feature.dashboard

import com.moazip.feature.dashboard.contract.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import com.moazip.core.ui.component.MoaZipButton
import com.moazip.core.ui.component.MoaZipCard
import java.text.NumberFormat
import java.util.Locale
import com.moazip.feature.dashboard.ui.component.LatestAssetRecordCard
import com.moazip.feature.dashboard.ui.component.AssetTrendCard

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
        LatestAssetRecordCard(snapshot = state.latestSnapshot)
        AssetTrendCard(points = state.assetTrend)
        MoaZipButton(
            text = "이번 달 월급 계획 보기",
            onClick = { onIntent(DashboardIntent.OpenMonthlyBudget) },
        )
    }
}

@Composable
private fun SummaryCard(label: String, value: String) {
    MoaZipCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(label)
            Text(value, fontWeight = FontWeight.SemiBold)
        }
    }
}

private fun Long.asWon(): String = NumberFormat.getCurrencyInstance(Locale.KOREA).format(this)
