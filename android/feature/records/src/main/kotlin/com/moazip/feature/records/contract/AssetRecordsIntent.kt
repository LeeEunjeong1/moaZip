package com.moazip.feature.records.contract

import com.moazip.core.presentation.mvi.UiIntent

sealed interface AssetRecordsIntent : UiIntent {
    data object RetryClicked : AssetRecordsIntent
}
