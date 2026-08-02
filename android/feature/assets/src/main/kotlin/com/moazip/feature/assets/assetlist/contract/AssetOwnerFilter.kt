package com.moazip.feature.assets.assetlist.contract

sealed interface AssetOwnerFilter {
    data object All : AssetOwnerFilter
    data object Common : AssetOwnerFilter
    data class Member(
        val userId: String,
        val displayName: String,
    ) : AssetOwnerFilter
}
