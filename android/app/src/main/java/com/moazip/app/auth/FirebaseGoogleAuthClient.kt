package com.moazip.app.auth

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.moazip.app.R
import com.moazip.core.domain.repository.UserRepository
import com.moazip.core.model.UserProfile
import com.moazip.feature.auth.GoogleLoginOutcome
import kotlinx.coroutines.tasks.await

class FirebaseGoogleAuthClient(
    private val activity: ComponentActivity,
    private val userRepository: UserRepository,
    private val credentialManager: CredentialManager = CredentialManager.create(activity),
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
) {
    fun hasAuthenticatedUser(): Boolean = firebaseAuth.currentUser != null

    suspend fun signIn(): GoogleLoginOutcome = try {
        val googleIdOption = GetSignInWithGoogleOption.Builder(
            activity.getString(R.string.default_web_client_id),
        )
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        val credential = credentialManager.getCredential(activity, request).credential
        if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val firebaseCredential = GoogleAuthProvider.getCredential(googleCredential.idToken, null)
            val authResult = firebaseAuth.signInWithCredential(firebaseCredential).await()
            val firebaseUser = authResult.user
            if (firebaseUser == null) {
                GoogleLoginOutcome.Failure
            } else {
                userRepository.createIfAbsent(
                    UserProfile(
                        uid = firebaseUser.uid,
                        email = firebaseUser.email,
                        displayName = firebaseUser.displayName,
                        photoUrl = firebaseUser.photoUrl?.toString(),
                    ),
                )
                GoogleLoginOutcome.Success
            }
        } else {
            GoogleLoginOutcome.Failure
        }
    } catch (exception: GetCredentialCancellationException) {
        Log.d(TAG, "Google sign-in was cancelled", exception)
        GoogleLoginOutcome.Cancelled
    } catch (exception: Exception) {
        Log.e(TAG, "Google sign-in failed", exception)
        GoogleLoginOutcome.Failure
    }

    private companion object {
        const val TAG = "FirebaseGoogleAuth"
    }
}
