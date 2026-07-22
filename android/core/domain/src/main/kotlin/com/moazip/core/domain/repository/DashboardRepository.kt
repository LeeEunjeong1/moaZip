package com.moazip.core.domain.repository

import com.moazip.core.model.DashboardSummary
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun observeSummary(): Flow<DashboardSummary>
}
