package com.moazip.core.domain.usecase

import com.moazip.core.domain.repository.AssetSnapshotRepository

class ObserveAssetSnapshotsUseCase(private val repository: AssetSnapshotRepository) {
    operator fun invoke(userId: String) = repository.observeSnapshots(userId)
}
