package com.moazip.feature.assets.editasset.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.moazip.feature.assets.R
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.assets.assetform.ui.AssetFormScreen
import com.moazip.feature.assets.editasset.contract.EditAssetError
import com.moazip.feature.assets.editasset.contract.EditAssetIntent
import com.moazip.feature.assets.editasset.contract.EditAssetState

@Composable
fun EditAssetScreen(state: EditAssetState, onIntent: (EditAssetIntent) -> Unit, modifier: Modifier = Modifier) {
    if (state.isLoading) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }
    AssetFormScreen(
        title = stringResource(R.string.edit_asset_title),
        description = stringResource(R.string.edit_asset_description),
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
        submitText = stringResource(R.string.edit_asset_save),
        submittingText = stringResource(R.string.edit_asset_saving),
        cancelText = stringResource(R.string.add_asset_cancel),
        canSubmit = state.canSave,
        isSubmitting = state.isSaving || state.isDeleting,
        errorMessage = state.error?.message(),
        onNameChanged = { onIntent(EditAssetIntent.NameChanged(it)) },
        onOwnerSelected = { onIntent(EditAssetIntent.OwnerSelected(it)) },
        onAssetTypeSelected = { onIntent(EditAssetIntent.AssetTypeSelected(it)) },
        onCategorySelected = { onIntent(EditAssetIntent.CategorySelected(it)) },
        onAmountChanged = { onIntent(EditAssetIntent.AmountChanged(it)) },
        onMemoChanged = { onIntent(EditAssetIntent.MemoChanged(it)) },
        onSubmitClick = { onIntent(EditAssetIntent.SaveClicked) },
        onCancelClick = { onIntent(EditAssetIntent.CancelClicked) },
        deleteText = if (state.isDeleting) {
            stringResource(R.string.edit_asset_deleting)
        } else {
            stringResource(R.string.edit_asset_delete)
        },
        onDeleteClick = { onIntent(EditAssetIntent.DeleteClicked) },
        modifier = modifier,
    )
    if (state.showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { onIntent(EditAssetIntent.DeleteDismissed) },
            containerColor = MoaZipPalette.White,
            title = { Text(stringResource(R.string.edit_asset_delete_dialog_title)) },
            text = { Text(stringResource(R.string.edit_asset_delete_dialog_description)) },
            confirmButton = {
                TextButton(onClick = { onIntent(EditAssetIntent.DeleteConfirmed) }) {
                    Text(
                        text = stringResource(R.string.edit_asset_delete_confirm),
                        color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { onIntent(EditAssetIntent.DeleteDismissed) }) {
                    Text(stringResource(R.string.edit_asset_delete_cancel))
                }
            },
        )
    }
}

@Composable
private fun EditAssetError.message() = stringResource(when (this) {
    EditAssetError.INVALID_INPUT -> R.string.add_asset_error_invalid_input
    EditAssetError.UNAUTHENTICATED -> R.string.add_asset_error_unauthenticated
    EditAssetError.LOAD_FAILED -> R.string.edit_asset_error_load_failed
    EditAssetError.SAVE_FAILED -> R.string.edit_asset_error_save_failed
    EditAssetError.DELETE_FAILED -> R.string.edit_asset_error_delete_failed
})
