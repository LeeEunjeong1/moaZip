package com.moazip.feature.assets.editasset

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.moazip.core.domain.auth.CurrentUserProvider
import com.moazip.core.domain.usecase.GetAssetUseCase
import com.moazip.core.domain.usecase.GetHouseholdMembersUseCase
import com.moazip.core.domain.usecase.UpdateAssetUseCase
import com.moazip.core.model.AssetKind
import com.moazip.core.model.NewAsset
import com.moazip.core.presentation.mvi.MviViewModel
import com.moazip.feature.assets.addasset.contract.AssetCategory
import com.moazip.feature.assets.addasset.contract.AssetType
import com.moazip.feature.assets.addasset.contract.OwnerSelection
import com.moazip.feature.assets.editasset.contract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditAssetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAssetUseCase: GetAssetUseCase,
    private val updateAssetUseCase: UpdateAssetUseCase,
    private val getHouseholdMembersUseCase: GetHouseholdMembersUseCase,
    private val currentUserProvider: CurrentUserProvider,
) : MviViewModel<EditAssetIntent, EditAssetState, EditAssetEffect>(EditAssetState()) {
    private val assetId: String = checkNotNull(savedStateHandle["assetId"])

    init { loadAsset() }

    override fun onIntent(intent: EditAssetIntent) = when (intent) {
        is EditAssetIntent.NameChanged -> reduce { copy(name = intent.value, error = null) }
        is EditAssetIntent.OwnerSelected -> reduce { copy(owner = intent.value, error = null) }
        is EditAssetIntent.AssetTypeSelected -> reduce { copy(assetType = intent.value, category = null, error = null) }
        is EditAssetIntent.CategorySelected -> reduce { copy(category = intent.value, error = null) }
        is EditAssetIntent.AmountChanged -> reduce { copy(amount = intent.value.filter(Char::isDigit), error = null) }
        is EditAssetIntent.MemoChanged -> reduce { copy(memo = intent.value, error = null) }
        EditAssetIntent.SaveClicked -> saveAsset()
        EditAssetIntent.DeleteClicked -> reduce { copy(showDeleteConfirmation = true, error = null) }
        EditAssetIntent.DeleteDismissed -> reduce { copy(showDeleteConfirmation = false) }
        EditAssetIntent.DeleteConfirmed -> deleteAsset()
        EditAssetIntent.CancelClicked -> postEffect(EditAssetEffect.NavigateBack)
    }

    private fun deleteAsset() {
        val userId = currentUserProvider.userId
            ?: return reduce { copy(showDeleteConfirmation = false, error = EditAssetError.UNAUTHENTICATED) }
        viewModelScope.launch {
            reduce { copy(showDeleteConfirmation = false, isDeleting = true, error = null) }
            runCatching { updateAssetUseCase.delete(userId, assetId) }
                .onSuccess {
                    reduce { copy(isDeleting = false) }
                    postEffect(EditAssetEffect.AssetDeleted)
                }
                .onFailure { reduce { copy(isDeleting = false, error = EditAssetError.DELETE_FAILED) } }
        }
    }

    private fun loadAsset() {
        val userId = currentUserProvider.userId ?: return reduce { copy(isLoading = false, error = EditAssetError.UNAUTHENTICATED) }
        viewModelScope.launch {
            runCatching {
                val asset = async { getAssetUseCase(userId, assetId) }
                val members = async { getHouseholdMembersUseCase(userId) }
                asset.await() to members.await()
            }.onSuccess { (asset, members) ->
                reduce {
                    copy(
                        name = asset.name,
                        members = members,
                        owner = asset.ownerId?.let { OwnerSelection.Member(it, asset.ownerName) } ?: OwnerSelection.Common,
                        assetType = asset.kind.toUiType(),
                        category = asset.category.toUiCategory(),
                        amount = asset.currentAmount.toString(),
                        memo = asset.memo,
                        isLoading = false,
                        error = null,
                    )
                }
            }.onFailure { reduce { copy(isLoading = false, error = EditAssetError.LOAD_FAILED) } }
        }
    }

    private fun saveAsset() {
        val current = state.value
        val userId = currentUserProvider.userId
        if (!current.canSave) return reduce { copy(error = EditAssetError.INVALID_INPUT) }
        if (userId == null) return reduce { copy(error = EditAssetError.UNAUTHENTICATED) }
        viewModelScope.launch {
            reduce { copy(isSaving = true, error = null) }
            runCatching {
                val owner = current.owner
                updateAssetUseCase(userId, assetId, NewAsset(
                    name = current.name,
                    ownerUserId = (owner as? OwnerSelection.Member)?.userId,
                    ownerDisplayName = (owner as? OwnerSelection.Member)?.displayName,
                    kind = current.assetType.toModel(),
                    category = checkNotNull(current.category).toModel(),
                    currentAmount = checkNotNull(current.amount.toLongOrNull()),
                    memo = current.memo,
                ))
            }.onSuccess {
                reduce { copy(isSaving = false) }
                postEffect(EditAssetEffect.AssetUpdated)
            }.onFailure { reduce { copy(isSaving = false, error = EditAssetError.SAVE_FAILED) } }
        }
    }

    private fun AssetKind.toUiType() = when (this) { AssetKind.ASSET -> AssetType.ASSET; AssetKind.INVESTMENT -> AssetType.INVESTMENT; AssetKind.LIABILITY -> AssetType.DEBT }
    private fun com.moazip.core.model.AssetCategory.toUiCategory() = AssetCategory.entries.firstOrNull { it.name == name } ?: AssetCategory.OTHER
    private fun AssetType.toModel() = when (this) { AssetType.ASSET -> AssetKind.ASSET; AssetType.INVESTMENT -> AssetKind.INVESTMENT; AssetType.DEBT -> AssetKind.LIABILITY }
    private fun AssetCategory.toModel() = com.moazip.core.model.AssetCategory.valueOf(name)
}
