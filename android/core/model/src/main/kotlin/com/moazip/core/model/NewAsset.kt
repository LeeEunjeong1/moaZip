package com.moazip.core.model

data class NewAsset(
    val name: String,
    val ownerUserId: String?,
    val ownerDisplayName: String?,
    val kind: AssetKind,
    val category: AssetCategory,
    val currentAmount: Long,
    val memo: String,
)
