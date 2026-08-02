package com.moazip.feature.assets.assetlist.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import com.moazip.core.model.AssetCategory
import com.moazip.core.ui.theme.MoaZipPalette

@Composable
internal fun AssetCategoryIcon(
    category: AssetCategory,
    contentDescription: String,
) {
    Icon(
        imageVector = category.icon(),
        contentDescription = contentDescription,
        modifier = Modifier.size(22.dp),
        tint = MoaZipPalette.Gray900,
    )
}

private fun AssetCategory.icon(): ImageVector = when (this) {
    AssetCategory.LEASE_DEPOSIT,
    AssetCategory.HOUSING_SUBSCRIPTION,
    -> Icons.Outlined.Home

    AssetCategory.SAVINGS,
    AssetCategory.DEPOSIT,
    -> Icons.Outlined.Savings

    AssetCategory.RETIREMENT -> Icons.Outlined.Work
    AssetCategory.CHECKING,
    AssetCategory.CASH,
    -> Icons.Outlined.AccountBalanceWallet

    AssetCategory.LOAN -> Icons.Outlined.CreditCard
    AssetCategory.ISA,
    AssetCategory.OVERSEAS_STOCK,
    AssetCategory.DOMESTIC_STOCK,
    AssetCategory.STOCK,
    -> Icons.AutoMirrored.Outlined.ShowChart

    AssetCategory.DIVIDEND -> Icons.Outlined.AttachMoney
    AssetCategory.OTHER,
    AssetCategory.ETC,
    -> Icons.Outlined.Category
}
