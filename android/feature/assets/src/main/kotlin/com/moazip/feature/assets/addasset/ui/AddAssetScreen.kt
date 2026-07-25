package com.moazip.feature.assets.addasset.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.assets.R
import com.moazip.feature.assets.addasset.contract.AddAssetIntent
import com.moazip.feature.assets.addasset.contract.AddAssetState
import com.moazip.feature.assets.addasset.ui.component.AddAssetActionButtons
import com.moazip.feature.assets.addasset.ui.component.AddAssetField
import com.moazip.feature.assets.addasset.ui.component.AddAssetHeader
import com.moazip.feature.assets.addasset.ui.component.CategorySelector
import com.moazip.feature.assets.addasset.ui.component.OwnerSelector
import com.moazip.feature.assets.addasset.ui.component.ThousandsSeparatorVisualTransformation

@Composable
fun AddAssetScreen(
    state: AddAssetState,
    onIntent: (AddAssetIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MoaZipPalette.Cream50),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 18.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AddAssetHeader()

            AddAssetField(
                label = stringResource(R.string.add_asset_name_label),
                value = state.name,
                placeholder = stringResource(R.string.add_asset_name_placeholder),
                onValueChange = { onIntent(AddAssetIntent.NameChanged(it)) },
            )
            OwnerSelector(
                memberNames = state.memberNames,
                selectedOwner = state.owner,
                onOwnerSelected = { onIntent(AddAssetIntent.OwnerSelected(it)) },
            )
            CategorySelector(
                selectedType = state.assetType,
                selectedCategory = state.category,
                availableCategories = state.availableCategories,
                onTypeSelected = { onIntent(AddAssetIntent.AssetTypeSelected(it)) },
                onCategorySelected = { onIntent(AddAssetIntent.CategorySelected(it)) },
            )
            AddAssetField(
                label = stringResource(R.string.add_asset_amount_label),
                value = state.amount,
                placeholder = stringResource(R.string.add_asset_amount_placeholder),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = ThousandsSeparatorVisualTransformation,
                onValueChange = { onIntent(AddAssetIntent.AmountChanged(it)) },
            )
            AddAssetField(
                label = stringResource(R.string.add_asset_memo_label),
                value = state.memo,
                placeholder = stringResource(R.string.add_asset_memo_placeholder),
                onValueChange = { onIntent(AddAssetIntent.MemoChanged(it)) },
            )
        }

        AddAssetActionButtons(
            canSave = state.canSave,
            isSaving = state.isSaving,
            errorMessage = state.errorMessage,
            onSaveClick = { onIntent(AddAssetIntent.SaveClicked) },
            onCancelClick = { onIntent(AddAssetIntent.CancelClicked) },
        )
    }
}
