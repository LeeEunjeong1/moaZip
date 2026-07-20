package com.moazip.core.model

data class Asset(
    val id: String,
    val ownerId: String,
    val kind: AssetKind,
    val category: AssetCategory,
    val institution: String,
    val name: String,
    val currentAmount: Long,
    val principal: Long? = null,
    val memo: String = "",
    val status: AssetStatus = AssetStatus.ACTIVE,
)

enum class AssetKind { ASSET, INVESTMENT, LIABILITY }

enum class AssetCategory {
    SAVINGS,
    RETIREMENT,
    HOUSING_SUBSCRIPTION,
    CASH,
    DEPOSIT,
    LOAN,
    ISA,
    STOCK,
    DIVIDEND,
    ETC,
}

enum class AssetStatus { ACTIVE, NEEDS_REVIEW, CLOSED }
