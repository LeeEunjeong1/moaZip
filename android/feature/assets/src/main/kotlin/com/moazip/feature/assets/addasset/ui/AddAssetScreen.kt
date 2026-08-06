package com.moazip.feature.assets.addasset.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.moazip.feature.assets.R
import com.moazip.feature.assets.addasset.contract.AddAssetIntent
import com.moazip.feature.assets.addasset.contract.AddAssetState
import com.moazip.feature.assets.assetform.ui.AssetFormScreen

@Composable
fun AddAssetScreen(
    state: AddAssetState,
    onIntent: (AddAssetIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    AssetFormScreen(
        title = stringResource(R.string.add_asset_title),
        description = stringResource(R.string.add_asset_description),
        nameLabel = stringResource(R.string.add_asset_name_label),
        namePlaceholder = stringResource(R.string.add_asset_name_placeholder),
        name = state.name,
        members = state.members,
        owner = state.owner,
        assetType = state.assetType,
        category = state.category,
        availableCategories = state.availableCategories,
        amountLabel = stringResource(R.string.add_asset_amount_label),
        amountPlaceholder = stringResource(R.string.add_asset_amount_placeholder),
        amount = state.amount,
        memoLabel = stringResource(R.string.add_asset_memo_label),
        memoPlaceholder = stringResource(R.string.add_asset_memo_placeholder),
        memo = state.memo,
        submitText = stringResource(R.string.add_asset_save),
        submittingText = stringResource(R.string.add_asset_saving),
        cancelText = stringResource(R.string.add_asset_cancel),
        canSubmit = state.canSave,
        isSubmitting = state.isSaving,
        errorMessage = state.error?.toMessage(),
        onNameChanged = { onIntent(AddAssetIntent.NameChanged(it)) },
        onOwnerSelected = { onIntent(AddAssetIntent.OwnerSelected(it)) },
        onAssetTypeSelected = { onIntent(AddAssetIntent.AssetTypeSelected(it)) },
        onCategorySelected = { onIntent(AddAssetIntent.CategorySelected(it)) },
        onAmountChanged = { onIntent(AddAssetIntent.AmountChanged(it)) },
        onMemoChanged = { onIntent(AddAssetIntent.MemoChanged(it)) },
        onSubmitClick = { onIntent(AddAssetIntent.SaveClicked) },
        onCancelClick = { onIntent(AddAssetIntent.CancelClicked) },
        modifier = modifier,
    )
}
