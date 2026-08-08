package com.moazip.feature.assets.assetform.ui

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.moazip.core.model.HouseholdMember
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.assets.addasset.contract.AssetCategory
import com.moazip.feature.assets.addasset.contract.AssetType
import com.moazip.feature.assets.addasset.contract.OwnerSelection
import com.moazip.feature.assets.addasset.ui.component.AddAssetField
import com.moazip.feature.assets.addasset.ui.component.CategorySelector
import com.moazip.feature.assets.addasset.ui.component.OwnerSelector
import com.moazip.feature.assets.addasset.ui.component.ThousandsSeparatorVisualTransformation
import com.moazip.feature.assets.assetform.ui.component.AssetFormActionButtons
import com.moazip.feature.assets.assetform.ui.component.AssetFormHeader

@Composable
fun AssetFormScreen(
    title: String,
    description: String,
    nameLabel: String,
    namePlaceholder: String,
    name: String,
    members: List<HouseholdMember>,
    owner: OwnerSelection,
    assetType: AssetType,
    category: AssetCategory?,
    availableCategories: List<AssetCategory>,
    amountLabel: String,
    amountPlaceholder: String,
    amount: String,
    memoLabel: String,
    memoPlaceholder: String,
    memo: String,
    submitText: String,
    submittingText: String,
    cancelText: String,
    canSubmit: Boolean,
    isSubmitting: Boolean,
    errorMessage: String?,
    onNameChanged: (String) -> Unit,
    onOwnerSelected: (OwnerSelection) -> Unit,
    onAssetTypeSelected: (AssetType) -> Unit,
    onCategorySelected: (AssetCategory) -> Unit,
    onAmountChanged: (String) -> Unit,
    onMemoChanged: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onCancelClick: () -> Unit,
    deleteText: String? = null,
    onDeleteClick: (() -> Unit)? = null,
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
            AssetFormHeader(title = title, description = description)
            AddAssetField(
                label = nameLabel,
                value = name,
                placeholder = namePlaceholder,
                onValueChange = onNameChanged,
            )
            OwnerSelector(
                members = members,
                selectedOwner = owner,
                onOwnerSelected = onOwnerSelected,
            )
            CategorySelector(
                selectedType = assetType,
                selectedCategory = category,
                availableCategories = availableCategories,
                onTypeSelected = onAssetTypeSelected,
                onCategorySelected = onCategorySelected,
            )
            AddAssetField(
                label = amountLabel,
                value = amount,
                placeholder = amountPlaceholder,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = ThousandsSeparatorVisualTransformation,
                onValueChange = onAmountChanged,
            )
            AddAssetField(
                label = memoLabel,
                value = memo,
                placeholder = memoPlaceholder,
                onValueChange = onMemoChanged,
            )
        }
        AssetFormActionButtons(
            submitText = submitText,
            submittingText = submittingText,
            cancelText = cancelText,
            canSubmit = canSubmit,
            isSubmitting = isSubmitting,
            errorMessage = errorMessage,
            onSubmitClick = onSubmitClick,
            onCancelClick = onCancelClick,
            deleteText = deleteText,
            onDeleteClick = onDeleteClick,
        )
    }
}
