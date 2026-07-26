package com.moazip.app.navigation.graph

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moazip.app.auth.GoogleSignInCoordinator
import com.moazip.app.navigation.AppRoute
import com.moazip.app.navigation.HouseholdGateScreen
import com.moazip.core.domain.auth.CurrentUserProvider
import com.moazip.core.domain.usecase.HasJoinedHouseholdUseCase
import com.moazip.feature.auth.LoginRoute
import com.moazip.feature.auth.LoginViewModel

internal fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    googleSignInCoordinator: GoogleSignInCoordinator,
    hasJoinedHouseholdUseCase: HasJoinedHouseholdUseCase,
    currentUserProvider: CurrentUserProvider,
) {
    composable(AppRoute.Login) {
        val viewModel: LoginViewModel = hiltViewModel()
        LoginRoute(
            viewModel = viewModel,
            onGoogleLoginRequested = googleSignInCoordinator::signIn,
            onLoginSucceeded = {
                navController.navigate(AppRoute.HouseholdGate) {
                    popUpTo(AppRoute.Login) { inclusive = true }
                }
            },
        )
    }

    composable(AppRoute.HouseholdGate) {
        LaunchedEffect(Unit) {
            val userId = currentUserProvider.userId
            val hasJoinedHousehold = userId != null && runCatching {
                hasJoinedHouseholdUseCase(userId)
            }.onFailure { exception ->
                Log.e(TAG, "Failed to restore household membership", exception)
            }.getOrDefault(false)
            val nextRoute = if (hasJoinedHousehold) {
                AppRoute.Dashboard
            } else {
                AppRoute.CreateHome
            }
            navController.navigate(nextRoute) {
                popUpTo(AppRoute.HouseholdGate) { inclusive = true }
            }
        }
        HouseholdGateScreen()
    }
}

private const val TAG = "HouseholdGate"
