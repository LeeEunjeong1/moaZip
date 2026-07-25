package com.moazip.core.domain.usecase

import com.moazip.core.domain.repository.AssetRepository
import com.moazip.core.model.NewAsset

class AddAssetUseCase(
    private val repository: AssetRepository,
) {
    suspend operator fun invoke(
        userId: String,
        asset: NewAsset,
    ): String = repository.addAsset(
        userId = userId,
        asset = asset,
    )
}
