package com.moazip.core.domain.usecase

import com.moazip.core.domain.repository.AssetRepository
import com.moazip.core.model.AssetKind
import com.moazip.core.model.DashboardSummary
import kotlinx.coroutines.flow.map

class ObserveDashboardSummary(
    private val repository: AssetRepository,
) {
    operator fun invoke(userId: String) = repository.observeAssets(userId).map { assets ->
        DashboardSummary(
            assetTotal = assets
                .filter { it.kind == AssetKind.ASSET }
                .sumOf { it.currentAmount },
            investmentTotal = assets
                .filter { it.kind == AssetKind.INVESTMENT }
                .sumOf { it.currentAmount },
            liabilityTotal = assets
                .filter { it.kind == AssetKind.LIABILITY }
                .sumOf { it.currentAmount },
        )
    }
}
