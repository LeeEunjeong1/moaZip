package com.moazip.core.domain.usecase

import com.moazip.core.domain.repository.DashboardRepository

class ObserveDashboardSummary(
    private val repository: DashboardRepository,
) {
    operator fun invoke() = repository.observeSummary()
}
