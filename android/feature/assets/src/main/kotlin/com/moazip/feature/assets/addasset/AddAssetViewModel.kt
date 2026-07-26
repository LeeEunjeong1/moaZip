package com.moazip.feature.assets.addasset

import androidx.lifecycle.viewModelScope
import com.moazip.core.domain.usecase.AddAssetUseCase
import com.moazip.core.model.AssetKind
import com.moazip.core.model.NewAsset
import com.moazip.core.presentation.mvi.MviViewModel
import com.moazip.feature.assets.addasset.contract.AddAssetEffect
import com.moazip.feature.assets.addasset.contract.AddAssetError
import com.moazip.feature.assets.addasset.contract.AddAssetIntent
import com.moazip.feature.assets.addasset.contract.AddAssetState
import com.moazip.feature.assets.addasset.contract.AssetCategory
import com.moazip.feature.assets.addasset.contract.AssetType
import com.moazip.feature.assets.addasset.contract.OwnerSelection
import kotlinx.coroutines.launch
import com.moazip.core.domain.auth.CurrentUserProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddAssetViewModel @Inject constructor(
    private val addAssetUseCase: AddAssetUseCase,
    private val currentUserProvider: CurrentUserProvider,
) : MviViewModel<AddAssetIntent, AddAssetState, AddAssetEffect>(
    initialState = AddAssetState(
        memberNames = listOfNotNull(currentUserProvider.displayName)
            .map(String::trim)
            .filter(String::isNotEmpty)
            .distinct(),
    ),
) {
    override fun onIntent(intent: AddAssetIntent) {
        when (intent) {
            is AddAssetIntent.NameChanged -> reduce { copy(name = intent.value, error = null) }
            is AddAssetIntent.OwnerSelected -> reduce { copy(owner = intent.value, error = null) }
            is AddAssetIntent.AssetTypeSelected -> reduce {
                copy(assetType = intent.value, category = null, error = null)
            }
            is AddAssetIntent.CategorySelected -> reduce {
                copy(category = intent.value, error = null)
            }
            is AddAssetIntent.AmountChanged -> reduce {
                copy(amount = intent.value.filter(Char::isDigit), error = null)
            }
            is AddAssetIntent.MemoChanged -> reduce { copy(memo = intent.value, error = null) }
            AddAssetIntent.SaveClicked -> saveAsset()
            AddAssetIntent.CancelClicked -> postEffect(AddAssetEffect.NavigateBack)
        }
    }

    private fun saveAsset() {
        val currentState = state.value
        val currentUserId = currentUserProvider.userId
        if (!currentState.canSave) {
            reduce { copy(error = AddAssetError.INVALID_INPUT) }
            return
        }
        if (currentUserId == null) {
            reduce { copy(error = AddAssetError.UNAUTHENTICATED) }
            return
        }

        val selectedCategory = checkNotNull(currentState.category)
        val amount = checkNotNull(currentState.amount.toLongOrNull())
        viewModelScope.launch {
            reduce { copy(isSaving = true, error = null) }
            runCatching {
                addAssetUseCase(
                    userId = currentUserId,
                    asset = NewAsset(
                        name = currentState.name,
                        ownerUserId = currentState.owner.userId(currentUserId),
                        ownerDisplayName = currentState.owner.displayName(),
                        kind = currentState.assetType.toModel(),
                        category = selectedCategory.toModel(),
                        currentAmount = amount,
                        memo = currentState.memo,
                    ),
                )
            }.onSuccess {
                reduce { copy(isSaving = false) }
                postEffect(AddAssetEffect.AssetSaved)
            }.onFailure {
                reduce {
                    copy(
                        isSaving = false,
                        error = AddAssetError.SAVE_FAILED,
                    )
                }
            }
        }
    }

    private fun OwnerSelection.userId(currentUserId: String): String? = when (this) {
        OwnerSelection.Common -> null
        is OwnerSelection.Member -> currentUserId
    }

    private fun OwnerSelection.displayName(): String? = when (this) {
        OwnerSelection.Common -> null
        is OwnerSelection.Member -> displayName
    }

    private fun AssetType.toModel(): AssetKind = when (this) {
        AssetType.ASSET -> AssetKind.ASSET
        AssetType.INVESTMENT -> AssetKind.INVESTMENT
        AssetType.DEBT -> AssetKind.LIABILITY
    }

    private fun AssetCategory.toModel(): com.moazip.core.model.AssetCategory = when (this) {
        AssetCategory.LEASE_DEPOSIT -> com.moazip.core.model.AssetCategory.LEASE_DEPOSIT
        AssetCategory.DEPOSIT -> com.moazip.core.model.AssetCategory.DEPOSIT
        AssetCategory.SAVINGS -> com.moazip.core.model.AssetCategory.SAVINGS
        AssetCategory.LOAN -> com.moazip.core.model.AssetCategory.LOAN
        AssetCategory.CHECKING -> com.moazip.core.model.AssetCategory.CHECKING
        AssetCategory.RETIREMENT -> com.moazip.core.model.AssetCategory.RETIREMENT
        AssetCategory.HOUSING_SUBSCRIPTION -> com.moazip.core.model.AssetCategory.HOUSING_SUBSCRIPTION
        AssetCategory.ISA -> com.moazip.core.model.AssetCategory.ISA
        AssetCategory.OVERSEAS_STOCK -> com.moazip.core.model.AssetCategory.OVERSEAS_STOCK
        AssetCategory.DOMESTIC_STOCK -> com.moazip.core.model.AssetCategory.DOMESTIC_STOCK
        AssetCategory.OTHER -> com.moazip.core.model.AssetCategory.OTHER
    }
}
