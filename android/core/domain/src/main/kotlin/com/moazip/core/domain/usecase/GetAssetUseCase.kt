package com.moazip.core.domain.usecase

import com.moazip.core.domain.repository.AssetRepository

class GetAssetUseCase(private val repository: AssetRepository) {
    suspend operator fun invoke(userId: String, assetId: String) =
        repository.getAsset(userId, assetId)
}
