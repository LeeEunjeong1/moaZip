package com.moazip.feature.assets.assetlist.contract

import com.moazip.core.model.AssetCategory
import com.moazip.core.presentation.mvi.UiState

data class AssetListState(
    val selectedFilter: AssetListFilter = AssetListFilter.ALL,
    val selectedOwnerFilter: AssetOwnerFilter = AssetOwnerFilter.All,
    val sortOption: AssetSortOption = AssetSortOption.DEFAULT,
    val assets: List<AssetListItemUiModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: AssetListError? = null,
) : UiState {
    val assetTotal: Long
        get() = assets
            .filter { it.kind == AssetListFilter.ASSET }
            .sumOf(AssetListItemUiModel::amount)

    val investmentTotal: Long
        get() = assets
            .filter { it.kind == AssetListFilter.INVESTMENT }
            .sumOf(AssetListItemUiModel::amount)

    val liabilityTotal: Long
        get() = assets
            .filter { it.kind == AssetListFilter.LIABILITY }
            .sumOf(AssetListItemUiModel::amount)

    val netWorth: Long
        get() = assetTotal + investmentTotal - liabilityTotal

    val recordedAtMillis: Long?
        get() = assets.mapNotNull(AssetListItemUiModel::recordedAtMillis).maxOrNull()

    val ownerFilters: List<AssetOwnerFilter>
        get() = buildList {
            add(AssetOwnerFilter.All)
            if (assets.any { it.ownerId == null }) add(AssetOwnerFilter.Common)
            assets
                .filter { it.ownerId != null }
                .distinctBy(AssetListItemUiModel::ownerId)
                .sortedBy { it.ownerName.orEmpty() }
                .forEach { asset ->
                    add(
                        AssetOwnerFilter.Member(
                            userId = checkNotNull(asset.ownerId),
                            displayName = asset.ownerName.orEmpty(),
                        ),
                    )
                }
        }

    val filteredAssets: List<AssetListItemUiModel>
        get() = assets
            .asSequence()
            .filter { selectedFilter == AssetListFilter.ALL || it.kind == selectedFilter }
            .filter { asset ->
                when (val ownerFilter = selectedOwnerFilter) {
                    AssetOwnerFilter.All -> true
                    AssetOwnerFilter.Common -> asset.ownerId == null
                    is AssetOwnerFilter.Member -> asset.ownerId == ownerFilter.userId
                }
            }
            .sortedWith(sortOption.comparator())
            .toList()
}

private fun AssetSortOption.comparator(): Comparator<AssetListItemUiModel> = when (this) {
    AssetSortOption.DEFAULT -> compareBy<AssetListItemUiModel>(
        { if (it.ownerId == null) 0 else 1 },
        { it.ownerName.orEmpty() },
        { it.kind.ordinal },
        { it.category.sortOrder() },
    ).thenByDescending(AssetListItemUiModel::amount)
        .thenBy(AssetListItemUiModel::name)

    AssetSortOption.AMOUNT_DESCENDING ->
        compareByDescending<AssetListItemUiModel>(AssetListItemUiModel::amount)
            .thenBy(AssetListItemUiModel::name)

    AssetSortOption.RETURN_RATE_DESCENDING ->
        compareByDescending<AssetListItemUiModel> { it.returnRate != null }
            .thenByDescending { it.returnRate ?: Double.NEGATIVE_INFINITY }
            .thenBy(AssetListItemUiModel::name)

    AssetSortOption.RECORDED_AT_DESCENDING ->
        compareByDescending<AssetListItemUiModel> { it.recordedAtMillis ?: Long.MIN_VALUE }
            .thenBy(AssetListItemUiModel::name)
}

private fun AssetCategory.sortOrder(): Int = when (this) {
    AssetCategory.LEASE_DEPOSIT -> 0
    AssetCategory.SAVINGS,
    AssetCategory.DEPOSIT,
    -> 1
    AssetCategory.RETIREMENT -> 2
    AssetCategory.HOUSING_SUBSCRIPTION -> 3
    AssetCategory.CHECKING,
    AssetCategory.CASH,
    -> 4
    AssetCategory.LOAN -> 5
    AssetCategory.ISA -> 6
    AssetCategory.DOMESTIC_STOCK -> 7
    AssetCategory.OVERSEAS_STOCK,
    AssetCategory.STOCK,
    -> 8
    AssetCategory.DIVIDEND -> 9
    AssetCategory.OTHER,
    AssetCategory.ETC,
    -> 10
}
