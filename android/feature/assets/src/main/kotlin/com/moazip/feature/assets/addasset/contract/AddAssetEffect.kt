package com.moazip.feature.assets.addasset.contract

import com.moazip.core.presentation.mvi.UiEffect

sealed interface AddAssetEffect : UiEffect {
    data object NavigateBack : AddAssetEffect
}
