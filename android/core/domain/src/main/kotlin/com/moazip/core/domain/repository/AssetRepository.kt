package com.moazip.core.domain.repository

import com.moazip.core.model.Asset
import com.moazip.core.model.NewAsset
import kotlinx.coroutines.flow.Flow

interface AssetRepository {
    fun observeAssets(userId: String): Flow<List<Asset>>

    suspend fun addAsset(
        userId: String,
        asset: NewAsset,
    ): String
}
