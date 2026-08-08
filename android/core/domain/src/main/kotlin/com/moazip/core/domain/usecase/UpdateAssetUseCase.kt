package com.moazip.core.domain.usecase

import com.moazip.core.domain.repository.AssetRepository
import com.moazip.core.model.NewAsset

class UpdateAssetUseCase(private val repository: AssetRepository) {
    suspend operator fun invoke(userId: String, assetId: String, asset: NewAsset) =
        repository.updateAsset(userId, assetId, asset)

    suspend fun delete(userId: String, assetId: String) =
        repository.deleteAsset(userId, assetId)
}
