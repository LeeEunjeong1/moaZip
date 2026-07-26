package com.moazip.feature.assets.addasset.contract

sealed interface OwnerSelection {
    data object Common : OwnerSelection
    data class Member(
        val userId: String,
        val displayName: String?,
    ) : OwnerSelection
}
