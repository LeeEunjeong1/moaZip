package com.moazip.feature.assets.assetform.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.component.MoaZipButton
import com.moazip.core.ui.component.MoaZipOutlinedButton
import com.moazip.core.ui.theme.MoaZipPalette

@Composable
internal fun AssetFormActionButtons(
    submitText: String,
    submittingText: String,
    cancelText: String,
    canSubmit: Boolean,
    isSubmitting: Boolean,
    errorMessage: String?,
    onSubmitClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MoaZipPalette.Cream50)
            .padding(horizontal = 24.dp)
            .padding(top = 8.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        MoaZipButton(
            text = if (isSubmitting) submittingText else submitText,
            onClick = onSubmitClick,
            enabled = canSubmit,
        )
        MoaZipOutlinedButton(
            text = cancelText,
            onClick = onCancelClick,
            enabled = !isSubmitting,
        )
    }
}
