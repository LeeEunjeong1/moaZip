package com.moazip.feature.assets.editasset.contract

import com.moazip.core.presentation.mvi.UiEffect

sealed interface EditAssetEffect : UiEffect {
    data object NavigateBack : EditAssetEffect
    data object AssetUpdated : EditAssetEffect
}
