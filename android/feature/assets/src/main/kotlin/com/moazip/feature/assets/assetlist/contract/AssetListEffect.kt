package com.moazip.feature.assets.assetlist.contract

import com.moazip.core.presentation.mvi.UiEffect

sealed interface AssetListEffect : UiEffect {
    data class NavigateToEditAsset(val assetId: String) : AssetListEffect
}
