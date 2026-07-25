package com.moazip.core.domain.repository

import com.moazip.core.model.NewAsset

interface AssetRepository {
    suspend fun addAsset(
        userId: String,
        asset: NewAsset,
    ): String
}
