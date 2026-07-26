package com.moazip.app

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.moazip.app.auth.GoogleSignInCoordinator
import com.moazip.app.navigation.MoaZipNavHost
import com.moazip.core.domain.auth.CurrentUserProvider
import com.moazip.core.domain.usecase.HasJoinedHouseholdUseCase
import com.moazip.core.ui.theme.MoaZipTheme

@Composable
fun MoaZipApp(
    googleSignInCoordinator: GoogleSignInCoordinator,
    hasJoinedHouseholdUseCase: HasJoinedHouseholdUseCase,
    currentUserProvider: CurrentUserProvider,
) {
    MoaZipTheme {
        Surface {
            MoaZipNavHost(
                googleSignInCoordinator = googleSignInCoordinator,
                hasJoinedHouseholdUseCase = hasJoinedHouseholdUseCase,
                currentUserProvider = currentUserProvider,
            )
        }
    }
}
