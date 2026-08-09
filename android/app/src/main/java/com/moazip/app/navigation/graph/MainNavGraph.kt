package com.moazip.app.navigation.graph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.imePadding
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moazip.app.navigation.AppRoute
import com.moazip.app.navigation.MainScaffold
import com.moazip.app.navigation.MainTab
import com.moazip.app.navigation.navigateToMainTab
import com.moazip.feature.assets.assetlist.route.AssetListRoute
import com.moazip.feature.assets.editasset.EditAssetViewModel
import com.moazip.feature.assets.editasset.contract.EditAssetEffect
import com.moazip.feature.assets.editasset.route.EditAssetRoute
import com.moazip.feature.assets.addasset.AddAssetViewModel
import com.moazip.feature.assets.addasset.contract.AddAssetEffect
import com.moazip.feature.assets.addasset.route.AddAssetRoute
import com.moazip.feature.dashboard.DashboardRoute
import com.moazip.feature.dashboard.DashboardViewModel
import com.moazip.feature.dashboard.contract.DashboardEffect
import com.moazip.feature.records.route.AssetRecordsRoute
import com.moazip.feature.settings.route.SettingsRoute
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    composable(AppRoute.Dashboard) {
        BackHandler { /* Keep the app open while the home tab is selected. */ }
        val viewModel: DashboardViewModel = hiltViewModel()
        LaunchedEffect(viewModel) {
            viewModel.effect
                .onEach { effect ->
                    when (effect) {
                        DashboardEffect.NavigateToAssets -> {
                            navController.navigateToMainTab(MainTab.Assets)
                        }
                        DashboardEffect.NavigateToRecords -> {
                            navController.navigateToMainTab(MainTab.Records)
                        }
                        is DashboardEffect.ShowMessage -> Unit
                    }
                }
                .launchIn(this)
        }
        MainScaffold(
            selectedTab = MainTab.Home,
            onTabSelected = navController::navigateToMainTab,
        ) { innerPadding ->
            DashboardRoute(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }

    composable(MainTab.Assets.route) {
        MainScaffold(
            selectedTab = MainTab.Assets,
            onTabSelected = navController::navigateToMainTab,
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                AssetListRoute(onEditAsset = { navController.navigate(AppRoute.editAsset(it)) })
            }
        }
    }

    composable(AppRoute.AddAsset) {
        val viewModel: AddAssetViewModel = hiltViewModel()
        LaunchedEffect(viewModel) {
            viewModel.effect
                .onEach { effect ->
                    if (
                        effect is AddAssetEffect.NavigateBack ||
                        effect is AddAssetEffect.AssetSaved
                    ) {
                        navController.navigateToMainTab(MainTab.Assets)
                    }
                }
                .launchIn(this)
        }
        AddAssetRoute(
            viewModel = viewModel,
            modifier = Modifier.safeDrawingPadding().imePadding(),
        )
    }

    composable(AppRoute.EditAssetPattern) {
        val viewModel: EditAssetViewModel = hiltViewModel()
        LaunchedEffect(viewModel) {
            viewModel.effect.onEach { effect ->
                when (effect) {
                    EditAssetEffect.NavigateBack,
                    EditAssetEffect.AssetUpdated,
                    EditAssetEffect.AssetDeleted,
                    -> navController.popBackStack()
                }
            }.launchIn(this)
        }
        EditAssetRoute(
            viewModel = viewModel,
            modifier = Modifier.safeDrawingPadding().imePadding(),
        )
    }

    composable(AppRoute.Records) {
        MainScaffold(
            selectedTab = MainTab.Records,
            onTabSelected = navController::navigateToMainTab,
        ) { innerPadding ->
            AssetRecordsRoute(modifier = Modifier.padding(innerPadding))
        }
    }
    composable(AppRoute.Settings) {
        MainScaffold(
            selectedTab = MainTab.Settings,
            onTabSelected = navController::navigateToMainTab,
        ) { innerPadding ->
            SettingsRoute(
                onLoggedOut = {
                    navController.navigate(AppRoute.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
