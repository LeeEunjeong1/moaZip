package com.moazip.feature.assets.assetlist

import androidx.lifecycle.viewModelScope
import com.moazip.core.domain.auth.CurrentUserProvider
import com.moazip.core.domain.usecase.ObserveAssetsUseCase
import com.moazip.core.model.Asset
import com.moazip.core.model.AssetKind
import com.moazip.core.presentation.mvi.MviViewModel
import com.moazip.feature.assets.assetlist.contract.AssetListError
import com.moazip.feature.assets.assetlist.contract.AssetListEffect
import com.moazip.feature.assets.assetlist.contract.AssetListFilter
import com.moazip.feature.assets.assetlist.contract.AssetListIntent
import com.moazip.feature.assets.assetlist.contract.AssetListItemUiModel
import com.moazip.feature.assets.assetlist.contract.AssetListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssetListViewModel @Inject constructor(
    private val observeAssetsUseCase: ObserveAssetsUseCase,
    private val currentUserProvider: CurrentUserProvider,
) : MviViewModel<AssetListIntent, AssetListState, AssetListEffect>(AssetListState()) {
    private var observeJob: Job? = null

    init {
        observeAssets()
    }

    override fun onIntent(intent: AssetListIntent) {
        when (intent) {
            is AssetListIntent.FilterSelected -> reduce {
                copy(selectedFilter = intent.filter)
            }
            is AssetListIntent.OwnerFilterSelected -> reduce {
                copy(selectedOwnerFilter = intent.filter)
            }
            is AssetListIntent.SortOptionSelected -> reduce {
                copy(sortOption = intent.option)
            }
            is AssetListIntent.AssetClicked -> postEffect(
                AssetListEffect.NavigateToEditAsset(intent.assetId),
            )
            AssetListIntent.RetryClicked -> observeAssets()
        }
    }

    private fun observeAssets() {
        observeJob?.cancel()
        val userId = currentUserProvider.userId
        if (userId == null) {
            reduce {
                copy(isLoading = false, error = AssetListError.UNAUTHENTICATED)
            }
            return
        }

        observeJob = viewModelScope.launch {
            observeAssetsUseCase(userId)
                .onStart {
                    reduce { copy(isLoading = true, error = null) }
                }
                .catch {
                    reduce { copy(isLoading = false, error = AssetListError.LOAD_FAILED) }
                }
                .collect { assets ->
                    reduce {
                        copy(
                            assets = assets.map { it.toUiModel() },
                            isLoading = false,
                            error = null,
                        )
                    }
                }
        }
    }

    private fun Asset.toUiModel() = AssetListItemUiModel(
        id = id,
        name = name,
        category = category,
        ownerId = ownerId,
        ownerName = ownerName,
        amount = currentAmount,
        profit = profit,
        returnRate = returnRate,
        recordedAtMillis = recordedAtMillis,
        kind = when (kind) {
            AssetKind.ASSET -> AssetListFilter.ASSET
            AssetKind.INVESTMENT -> AssetListFilter.INVESTMENT
            AssetKind.LIABILITY -> AssetListFilter.LIABILITY
        },
    )
}
