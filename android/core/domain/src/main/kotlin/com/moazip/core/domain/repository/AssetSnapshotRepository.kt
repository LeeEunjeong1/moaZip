package com.moazip.core.domain.repository

import com.moazip.core.model.AssetSnapshot
import kotlinx.coroutines.flow.Flow

interface AssetSnapshotRepository {
    fun observeSnapshots(userId: String): Flow<List<AssetSnapshot>>
    suspend fun saveMonthlySnapshot(userId: String, snapshot: AssetSnapshot)
}
