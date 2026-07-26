package com.moazip.app.auth

import android.util.Log
import com.moazip.app.auth.contract.GoogleCredentialResult
import com.moazip.core.domain.repository.AuthRepository
import com.moazip.feature.auth.contract.GoogleLoginOutcome
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class GoogleSignInCoordinator @Inject constructor(
    private val credentialClient: GoogleCredentialClient,
    private val authRepository: AuthRepository,
) {
    fun hasAuthenticatedUser(): Boolean = authRepository.isAuthenticated

    suspend fun signIn(): GoogleLoginOutcome =
        when (val credentialResult = credentialClient.getGoogleIdToken()) {
            is GoogleCredentialResult.Success -> authenticate(credentialResult.idToken)
            GoogleCredentialResult.Cancelled -> GoogleLoginOutcome.Cancelled
            GoogleCredentialResult.Failure -> GoogleLoginOutcome.Failure
        }

    private suspend fun authenticate(idToken: String): GoogleLoginOutcome = runCatching {
        authRepository.signInWithGoogle(idToken)
        GoogleLoginOutcome.Success
    }.onFailure { exception ->
        Log.e(TAG, "Firebase authentication failed", exception)
    }.getOrDefault(GoogleLoginOutcome.Failure)

    private companion object {
        const val TAG = "GoogleSignIn"
    }
}
