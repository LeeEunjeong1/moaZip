package com.moazip.feature.assets.addasset

import androidx.lifecycle.viewModelScope
import com.moazip.core.domain.usecase.AddAssetUseCase
import com.moazip.core.model.AssetKind
import com.moazip.core.model.NewAsset
import com.moazip.core.presentation.mvi.MviViewModel
import com.moazip.feature.assets.addasset.contract.AddAssetEffect
import com.moazip.feature.assets.addasset.contract.AddAssetIntent
import com.moazip.feature.assets.addasset.contract.AddAssetState
import com.moazip.feature.assets.addasset.contract.AssetCategory
import com.moazip.feature.assets.addasset.contract.AssetType
import com.moazip.feature.assets.addasset.contract.OwnerSelection
import kotlinx.coroutines.launch

class AddAssetViewModel(
    memberNames: List<String>,
    private val addAssetUseCase: AddAssetUseCase,
    private val currentUserIdProvider: () -> String?,
) : MviViewModel<AddAssetIntent, AddAssetState, AddAssetEffect>(
    initialState = AddAssetState(
        memberNames = memberNames
            .map(String::trim)
            .filter(String::isNotEmpty)
            .distinct(),
    ),
) {
    override fun onIntent(intent: AddAssetIntent) {
        when (intent) {
            is AddAssetIntent.NameChanged -> reduce { copy(name = intent.value, errorMessage = null) }
            is AddAssetIntent.OwnerSelected -> reduce { copy(owner = intent.value, errorMessage = null) }
            is AddAssetIntent.AssetTypeSelected -> reduce {
                copy(assetType = intent.value, category = null, errorMessage = null)
            }
            is AddAssetIntent.CategorySelected -> reduce {
                copy(category = intent.value, errorMessage = null)
            }
            is AddAssetIntent.AmountChanged -> reduce {
                copy(amount = intent.value.filter(Char::isDigit), errorMessage = null)
            }
            is AddAssetIntent.MemoChanged -> reduce { copy(memo = intent.value, errorMessage = null) }
            AddAssetIntent.SaveClicked -> saveAsset()
            AddAssetIntent.CancelClicked -> postEffect(AddAssetEffect.NavigateBack)
        }
    }

    private fun saveAsset() {
        val currentState = state.value
        val currentUserId = currentUserIdProvider()
        if (!currentState.canSave) {
            reduce { copy(errorMessage = "필수 항목을 모두 입력해 주세요.") }
            return
        }
        if (currentUserId == null) {
            reduce { copy(errorMessage = "로그인 정보를 확인할 수 없어요. 다시 로그인해 주세요.") }
            return
        }

        val selectedCategory = checkNotNull(currentState.category)
        val amount = checkNotNull(currentState.amount.toLongOrNull())
        viewModelScope.launch {
            reduce { copy(isSaving = true, errorMessage = null) }
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
                        errorMessage = "자산을 저장하지 못했어요. 잠시 후 다시 시도해 주세요.",
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
