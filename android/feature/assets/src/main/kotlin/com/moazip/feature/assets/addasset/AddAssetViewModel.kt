package com.moazip.feature.assets.addasset

import com.moazip.core.presentation.mvi.MviViewModel
import com.moazip.feature.assets.addasset.contract.AddAssetEffect
import com.moazip.feature.assets.addasset.contract.AddAssetIntent
import com.moazip.feature.assets.addasset.contract.AddAssetState

class AddAssetViewModel(
    memberNames: List<String>,
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
            is AddAssetIntent.NameChanged -> reduce { copy(name = intent.value) }
            is AddAssetIntent.OwnerSelected -> reduce { copy(owner = intent.value) }
            is AddAssetIntent.AssetTypeSelected -> reduce {
                copy(assetType = intent.value, category = null)
            }
            is AddAssetIntent.CategorySelected -> reduce { copy(category = intent.value) }
            is AddAssetIntent.AmountChanged -> reduce { copy(amount = intent.value.filter(Char::isDigit)) }
            is AddAssetIntent.MemoChanged -> reduce { copy(memo = intent.value) }
            AddAssetIntent.SaveClicked -> Unit
            AddAssetIntent.CancelClicked -> postEffect(AddAssetEffect.NavigateBack)
        }
    }
}
