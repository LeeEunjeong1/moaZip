package com.moazip.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.moazip.app.auth.GoogleSignInCoordinator
import com.moazip.app.navigation.graph.authGraph
import com.moazip.app.navigation.graph.mainGraph
import com.moazip.app.navigation.graph.partnerGraph
import com.moazip.core.domain.auth.CurrentUserProvider
import com.moazip.core.domain.usecase.HasJoinedHouseholdUseCase

@Composable
internal fun MoaZipNavHost(
    googleSignInCoordinator: GoogleSignInCoordinator,
    hasJoinedHouseholdUseCase: HasJoinedHouseholdUseCase,
    currentUserProvider: CurrentUserProvider,
) {
    val navController = rememberNavController()
    val startDestination = remember(googleSignInCoordinator) {
        if (googleSignInCoordinator.hasAuthenticatedUser()) {
            AppRoute.HouseholdGate
        } else {
            AppRoute.Login
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        authGraph(
            navController = navController,
            googleSignInCoordinator = googleSignInCoordinator,
            hasJoinedHouseholdUseCase = hasJoinedHouseholdUseCase,
            currentUserProvider = currentUserProvider,
        )
        partnerGraph(navController)
        mainGraph(navController)
    }
}
