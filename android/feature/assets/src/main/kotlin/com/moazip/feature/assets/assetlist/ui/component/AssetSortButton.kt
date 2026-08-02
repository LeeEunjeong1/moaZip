package com.moazip.feature.assets.assetlist.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.feature.assets.R
import com.moazip.feature.assets.assetlist.contract.AssetSortOption

@Composable
internal fun AssetSortButton(
    selectedOption: AssetSortOption,
    onOptionSelected: (AssetSortOption) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(
            onClick = { expanded = true },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MoaZipPalette.Beige200),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MoaZipPalette.White,
                contentColor = MoaZipPalette.Gray900,
            ),
            contentPadding = PaddingValues(horizontal = 12.dp),
            modifier = Modifier.heightIn(min = 40.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Sort,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = selectedOption.label(),
                style = MaterialTheme.typography.labelMedium,
            )
        }
        DropdownMenu(
            modifier = Modifier.background(MoaZipPalette.White),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            AssetSortOption.entries.forEach { option ->
                val selected = option == selectedOption
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.label(),
                            color = if (selected) MoaZipPalette.Gray950 else MoaZipPalette.Gray900,
                        )
                    },
                    onClick = {
                        expanded = false
                        onOptionSelected(option)
                    },
                    trailingIcon = if (selected) {
                        {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = null,
                                tint = MoaZipPalette.Yellow500,
                            )
                        }
                    } else {
                        null
                    },
                    modifier = Modifier.background(
                        if (selected) MoaZipPalette.Yellow50 else Color.Transparent,
                    ),
                )
            }
        }
    }
}

@Composable
private fun AssetSortOption.label(): String = stringResource(
    when (this) {
        AssetSortOption.DEFAULT -> R.string.asset_list_sort_default
        AssetSortOption.AMOUNT_DESCENDING -> R.string.asset_list_sort_amount
        AssetSortOption.RETURN_RATE_DESCENDING -> R.string.asset_list_sort_return_rate
        AssetSortOption.RECORDED_AT_DESCENDING -> R.string.asset_list_sort_recorded_at
    },
)
