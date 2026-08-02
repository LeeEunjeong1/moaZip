package com.moazip.feature.assets.assetlist.contract

data class AssetListItemUiModel(
    val id: String,
    val name: String,
    val categoryName: String,
    val ownerName: String,
    val amount: Long,
    val kind: AssetListFilter,
)
