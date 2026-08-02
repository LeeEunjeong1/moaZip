package com.moazip.core.domain.usecase

import com.moazip.core.domain.repository.AssetRepository
import com.moazip.core.model.Asset
import kotlinx.coroutines.flow.Flow

class ObserveAssetsUseCase(
    private val repository: AssetRepository,
) {
    operator fun invoke(userId: String): Flow<List<Asset>> = repository.observeAssets(userId)
}
