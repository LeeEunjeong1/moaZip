package com.moazip.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.moazip.app.auth.FirebaseGoogleAuthClient
import com.moazip.core.domain.auth.CurrentUserProvider
import com.moazip.core.domain.usecase.HasJoinedHouseholdUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var googleAuthClient: FirebaseGoogleAuthClient

    @Inject
    lateinit var hasJoinedHouseholdUseCase: HasJoinedHouseholdUseCase

    @Inject
    lateinit var currentUserProvider: CurrentUserProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoaZipApp(
                googleAuthClient = googleAuthClient,
                hasJoinedHouseholdUseCase = hasJoinedHouseholdUseCase,
                currentUserProvider = currentUserProvider,
            )
        }
    }
}
