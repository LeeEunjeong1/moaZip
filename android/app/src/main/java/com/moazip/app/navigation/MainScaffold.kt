package com.moazip.app.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moazip.app.R
import com.moazip.core.ui.component.MoaZipBottomBar
import com.moazip.core.ui.component.MoaZipBottomBarItem
import com.moazip.core.ui.theme.MoaZipPalette

@Composable
internal fun MainScaffold(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        containerColor = MoaZipPalette.Cream50,
        bottomBar = {
            Box(
                modifier = Modifier
                    .background(MoaZipPalette.Cream50)
                    .padding(horizontal = 24.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                MoaZipBottomBar {
                    MainNavigationItem(
                        tab = MainTab.Home,
                        labelRes = R.string.bottom_navigation_home,
                        selectedTab = selectedTab,
                        onTabSelected = onTabSelected,
                    )
                    MainNavigationItem(
                        tab = MainTab.Assets,
                        labelRes = R.string.bottom_navigation_assets,
                        selectedTab = selectedTab,
                        onTabSelected = onTabSelected,
                    )
                    MoaZipBottomBarItem(
                        selected = selectedTab == MainTab.Add,
                        label = null,
                        onClick = { onTabSelected(MainTab.Add) },
                        prominent = true,
                    ) {
                        Text(
                            text = "+",
                            color = MoaZipPalette.Gray900,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                    MainNavigationItem(
                        tab = MainTab.Records,
                        labelRes = R.string.bottom_navigation_records,
                        selectedTab = selectedTab,
                        onTabSelected = onTabSelected,
                    )
                    MainNavigationItem(
                        tab = MainTab.Settings,
                        labelRes = R.string.bottom_navigation_settings,
                        selectedTab = selectedTab,
                        onTabSelected = onTabSelected,
                    )
                }
            }
        },
        content = content,
    )
}

@Composable
private fun RowScope.MainNavigationItem(
    tab: MainTab,
    @StringRes labelRes: Int,
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
) {
    MoaZipBottomBarItem(
        selected = selectedTab == tab,
        label = stringResource(labelRes),
        onClick = { onTabSelected(tab) },
    )
}

@Composable
internal fun MainTabPlaceholder(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(titleRes),
            color = MoaZipPalette.Gray900,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
