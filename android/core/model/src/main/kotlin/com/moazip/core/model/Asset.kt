package com.moazip.core.model

data class Asset(
    val id: String,
    val householdId: String,
    val ownerId: String?,
    val ownerName: String?,
    val kind: AssetKind,
    val category: AssetCategory,
    val name: String,
    val currentAmount: Long,
    val memo: String = "",
    val status: AssetStatus = AssetStatus.ACTIVE,
    val updatedAtMillis: Long = 0L,
)

enum class AssetKind { ASSET, INVESTMENT, LIABILITY }

enum class AssetCategory {
    LEASE_DEPOSIT,
    SAVINGS,
    RETIREMENT,
    HOUSING_SUBSCRIPTION,
    CHECKING,
    CASH,
    DEPOSIT,
    LOAN,
    ISA,
    OVERSEAS_STOCK,
    DOMESTIC_STOCK,
    STOCK,
    DIVIDEND,
    OTHER,
    ETC,
}

enum class AssetStatus { ACTIVE, NEEDS_REVIEW, CLOSED }
