package com.moazip.core.domain.usecase

import com.moazip.core.domain.repository.AssetRepository
import com.moazip.core.domain.repository.AssetSnapshotRepository
import com.moazip.core.model.AssetKind
import com.moazip.core.model.AssetSnapshot
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import kotlinx.coroutines.flow.first

class RecordMonthlyAssetSnapshotUseCase(
    private val assetRepository: AssetRepository,
    private val snapshotRepository: AssetSnapshotRepository,
) {
    suspend operator fun invoke(userId: String, recordedAtMillis: Long = System.currentTimeMillis()) {
        val assets = assetRepository.observeAssets(userId).first()
        val monthKey = YearMonth.from(
            Instant.ofEpochMilli(recordedAtMillis).atZone(ZoneId.systemDefault()),
        ).toString()
        snapshotRepository.saveMonthlySnapshot(
            userId = userId,
            snapshot = AssetSnapshot(
                id = monthKey,
                monthKey = monthKey,
                assetTotal = assets.filter { it.kind == AssetKind.ASSET }.sumOf { it.currentAmount },
                investmentTotal = assets.filter { it.kind == AssetKind.INVESTMENT }.sumOf { it.currentAmount },
                liabilityTotal = assets.filter { it.kind == AssetKind.LIABILITY }.sumOf { it.currentAmount },
                recordedAtMillis = recordedAtMillis,
            ),
        )
    }
}
