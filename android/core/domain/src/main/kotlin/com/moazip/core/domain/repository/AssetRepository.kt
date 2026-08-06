package com.moazip.core.domain.repository

import com.moazip.core.model.Asset
import com.moazip.core.model.NewAsset
import kotlinx.coroutines.flow.Flow

interface AssetRepository {
    fun observeAssets(userId: String): Flow<List<Asset>>

    suspend fun getAsset(userId: String, assetId: String): Asset

    suspend fun addAsset(
        userId: String,
        asset: NewAsset,
    ): String

    suspend fun updateAsset(userId: String, assetId: String, asset: NewAsset)
}
