package com.moazip.feature.assets.addasset.contract

sealed interface OwnerSelection {
    data object Common : OwnerSelection
    data class Member(val displayName: String) : OwnerSelection
}
