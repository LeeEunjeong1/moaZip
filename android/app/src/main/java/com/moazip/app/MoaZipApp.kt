package com.moazip.app

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.moazip.app.auth.FirebaseGoogleAuthClient
import com.moazip.app.navigation.MoaZipNavHost
import com.moazip.core.domain.auth.CurrentUserProvider
import com.moazip.core.domain.usecase.HasJoinedHouseholdUseCase
import com.moazip.core.ui.theme.MoaZipTheme

@Composable
fun MoaZipApp(
    googleAuthClient: FirebaseGoogleAuthClient,
    hasJoinedHouseholdUseCase: HasJoinedHouseholdUseCase,
    currentUserProvider: CurrentUserProvider,
) {
    MoaZipTheme {
        Surface {
            MoaZipNavHost(
                googleAuthClient = googleAuthClient,
                hasJoinedHouseholdUseCase = hasJoinedHouseholdUseCase,
                currentUserProvider = currentUserProvider,
            )
        }
    }
}
