package com.moazip.feature.dashboard

import com.moazip.feature.dashboard.contract.*
import androidx.lifecycle.viewModelScope
import com.moazip.core.model.AssetSnapshot
import com.moazip.core.domain.usecase.ObserveDashboardSummary
import com.moazip.core.domain.usecase.ObserveAssetSnapshotsUseCase
import com.moazip.core.presentation.mvi.MviViewModel
import com.moazip.feature.dashboard.contract.DashboardAssetTrendPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.moazip.core.domain.auth.CurrentUserProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import java.time.YearMonth
import java.time.temporal.ChronoUnit

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val observeDashboardSummary: ObserveDashboardSummary,
    private val observeAssetSnapshotsUseCase: ObserveAssetSnapshotsUseCase,
    private val currentUserProvider: CurrentUserProvider,
) : MviViewModel<DashboardIntent, DashboardState, DashboardEffect>(DashboardState()) {
    private var observeJob: Job? = null
    private var observeSnapshotsJob: Job? = null

    init {
        observeSummary()
        observeLatestSnapshot()
    }

    override fun onIntent(intent: DashboardIntent) {
        when (intent) {
            DashboardIntent.Refresh -> {
                observeSummary()
                observeLatestSnapshot()
            }
            DashboardIntent.OpenAssets -> postEffect(DashboardEffect.NavigateToAssets)
            DashboardIntent.OpenRecords -> postEffect(DashboardEffect.NavigateToRecords)
            DashboardIntent.OpenMonthlyBudget -> postEffect(DashboardEffect.NavigateToMonthlyBudget)
        }
    }

    private fun observeLatestSnapshot() {
        observeSnapshotsJob?.cancel()
        val userId = currentUserProvider.userId ?: return
        observeSnapshotsJob = viewModelScope.launch {
            observeAssetSnapshotsUseCase(userId)
                .catch { /* Current asset summary can still be displayed. */ }
                .collect { snapshots ->
                    val sortedSnapshots = snapshots.sortedBy { it.monthKey }
                    reduce {
                        copy(
                            latestSnapshot = sortedSnapshots.lastOrNull(),
                            assetTrend = sortedSnapshots.toAssetTrend(),
                        )
                    }
                }
        }
    }

    private fun observeSummary() {
        observeJob?.cancel()
        val userId = currentUserProvider.userId
        if (userId == null) {
            reduce {
                copy(isLoading = false, errorMessage = "로그인 정보를 확인할 수 없어요.")
            }
            return
        }
        observeJob = viewModelScope.launch {
            observeDashboardSummary(userId)
                .onStart { reduce { copy(isLoading = true, errorMessage = null) } }
                .catch { error ->
                    val message = error.message ?: "자산 정보를 불러오지 못했어요."
                    reduce { copy(isLoading = false, errorMessage = message) }
                    postEffect(DashboardEffect.ShowMessage(message))
                }
                .collect { summary ->
                    reduce { copy(isLoading = false, summary = summary, errorMessage = null) }
                }
        }
    }

    private fun List<AssetSnapshot>.toAssetTrend(): List<DashboardAssetTrendPoint> {
        val recentSnapshots = takeLast(MAX_TREND_RECORDS)
        val actualPoints = recentSnapshots.map { snapshot ->
            val month = YearMonth.parse(snapshot.monthKey)
            DashboardAssetTrendPoint(
                monthKey = snapshot.monthKey,
                label = month.toChartLabel(),
                netWorth = snapshot.netWorth,
            )
        }
        if (recentSnapshots.size < 2) return actualPoints

        val monthlyChanges = recentSnapshots
            .takeLast(FORECAST_SOURCE_RECORDS)
            .zipWithNext { previous, current ->
                val previousMonth = YearMonth.parse(previous.monthKey)
                val currentMonth = YearMonth.parse(current.monthKey)
                val monthGap = ChronoUnit.MONTHS
                    .between(previousMonth, currentMonth)
                    .coerceAtLeast(1L)
                (current.netWorth - previous.netWorth) / monthGap
            }
        val averageMonthlyChange = monthlyChanges.average().toLong()
        val lastSnapshot = recentSnapshots.last()
        val forecastMonth = YearMonth.parse(lastSnapshot.monthKey).plusMonths(1)
        return actualPoints + DashboardAssetTrendPoint(
            monthKey = forecastMonth.toString(),
            label = "${forecastMonth.toChartLabel()} 예상",
            netWorth = (lastSnapshot.netWorth + averageMonthlyChange).coerceAtLeast(0L),
            isForecast = true,
        )
    }

    private fun YearMonth.toChartLabel(): String = "${year % 100}.${monthValue}"

    private companion object {
        const val MAX_TREND_RECORDS = 6
        const val FORECAST_SOURCE_RECORDS = 5
    }
}
