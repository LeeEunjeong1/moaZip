package com.moazip.feature.records

import com.moazip.core.presentation.mvi.MviViewModel
import androidx.lifecycle.viewModelScope
import com.moazip.core.domain.auth.CurrentUserProvider
import com.moazip.core.domain.usecase.ObserveAssetSnapshotsUseCase
import com.moazip.core.model.AssetSnapshot
import com.moazip.feature.records.contract.AssetRecordsEffect
import com.moazip.feature.records.contract.AssetRecordsError
import com.moazip.feature.records.contract.AssetRecordsIntent
import com.moazip.feature.records.contract.AssetRecordsState
import com.moazip.feature.records.contract.QuarterRecordUiModel
import com.moazip.feature.records.contract.SnapshotRecordUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.YearMonth
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssetRecordsViewModel @Inject constructor(
    private val observeAssetSnapshotsUseCase: ObserveAssetSnapshotsUseCase,
    private val currentUserProvider: CurrentUserProvider,
) :
    MviViewModel<AssetRecordsIntent, AssetRecordsState, AssetRecordsEffect>(AssetRecordsState()) {
    private var observeJob: Job? = null

    init { observeRecords() }

    override fun onIntent(intent: AssetRecordsIntent) {
        when (intent) {
            AssetRecordsIntent.RetryClicked -> observeRecords()
        }
    }

    private fun observeRecords() {
        observeJob?.cancel()
        val userId = currentUserProvider.userId
        if (userId == null) {
            reduce { copy(isLoading = false, error = AssetRecordsError.UNAUTHENTICATED) }
            return
        }
        observeJob = viewModelScope.launch {
            observeAssetSnapshotsUseCase(userId)
                .onStart { reduce { copy(isLoading = true, error = null) } }
                .catch { reduce { copy(isLoading = false, error = AssetRecordsError.LOAD_FAILED) } }
                .collect { snapshots -> reduce { snapshots.toState() } }
        }
    }

    private fun List<AssetSnapshot>.toState(): AssetRecordsState {
        val records = groupBy { snapshot -> snapshot.monthKey.toQuarterIndex() }
            .mapValues { (_, snapshots) -> snapshots.maxBy { it.monthKey } }
            .toSortedMap()
            .entries
            .toList()
            .takeLast(5)
        val latest = records.lastOrNull()
        val chronologicalSnapshots = sortedBy { it.monthKey }
        val previousSnapshot = chronologicalSnapshots.dropLast(1).lastOrNull()
        return if (latest == null) {
            AssetRecordsState(
                snapshotRecords = toSnapshotRecords(),
                isLoading = false,
            )
        } else {
            val quarterIndex = latest.key
            AssetRecordsState(
                selectedQuarter = quarterIndex.toTitle(),
                netWorth = latest.value.netWorth,
                previousRecordGrowthRate = previousSnapshot
                    ?.netWorth
                    ?.takeIf { it != 0L }
                    ?.let { previousNetWorth ->
                        (latest.value.netWorth - previousNetWorth).toDouble() /
                            kotlin.math.abs(previousNetWorth).toDouble() * 100.0
                    },
                quarterRecords = records.mapIndexed { index, (_, snapshot) ->
                    val previousNetWorth = records.getOrNull(index - 1)?.value?.netWorth
                    QuarterRecordUiModel(
                        label = snapshot.toRecordedDateLabel(),
                        netWorth = snapshot.netWorth,
                        growthRate = previousNetWorth
                            ?.takeIf { it != 0L }
                            ?.let {
                                (snapshot.netWorth - it).toDouble() /
                                    kotlin.math.abs(it).toDouble() * 100.0
                            },
                    )
                },
                snapshotRecords = toSnapshotRecords(),
                isLoading = false,
            )
        }
    }

    private fun String.toQuarterIndex(): Int {
        val month = YearMonth.parse(this)
        return month.year * 4 + (month.monthValue - 1) / 3
    }

    private fun Int.toTitle(): String = "${this / 4}년 ${this % 4 + 1}분기"

    private fun AssetSnapshot.toRecordedDateLabel(): String {
        if (recordedAtMillis <= 0L) return monthKey.replace("-", ".")
        return Instant.ofEpochMilli(recordedAtMillis)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yy.MM\ndd", Locale.KOREA))
    }

    private fun List<AssetSnapshot>.toSnapshotRecords(): List<SnapshotRecordUiModel> =
        sortedWith(compareBy<AssetSnapshot> { it.recordedAtMillis }.thenBy { it.monthKey })
            .mapIndexed { index, snapshot ->
                val previousNetWorth = getOrNull(index - 1)?.netWorth
                SnapshotRecordUiModel(
                    id = snapshot.id,
                    recordedDate = snapshot.toFullRecordedDate(),
                    netWorth = snapshot.netWorth,
                    financialAssetTotal = snapshot.financialAssetTotal,
                    depositTotal = snapshot.depositTotal,
                    liabilityTotal = snapshot.liabilityTotal,
                    growthRate = previousNetWorth
                        ?.takeIf { it != 0L }
                        ?.let {
                            (snapshot.netWorth - it).toDouble() /
                                kotlin.math.abs(it).toDouble() * 100.0
                        },
                )
            }
            .asReversed()

    private fun AssetSnapshot.toFullRecordedDate(): String {
        if (recordedAtMillis <= 0L) return monthKey.replace("-", ".")
        return Instant.ofEpochMilli(recordedAtMillis)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyy년 M월 d일", Locale.KOREA))
    }
}
