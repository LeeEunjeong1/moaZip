package com.moazip.feature.assets.assetlist.contract

import com.moazip.core.model.AssetCategory

data class AssetListItemUiModel(
    val id: String,
    val name: String,
    val category: AssetCategory,
    val ownerId: String? = null,
    val ownerName: String?,
    val amount: Long,
    val kind: AssetListFilter,
    val profit: Long? = null,
    val returnRate: Double? = null,
    val recordedAtMillis: Long? = null,
)
