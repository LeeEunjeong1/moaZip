package com.moazip.app.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.moazip.app.R
import com.moazip.app.auth.contract.GoogleCredentialResult
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class GoogleCredentialClient @Inject constructor(
    @param:ActivityContext private val activityContext: Context,
) {
    private val credentialManager = CredentialManager.create(activityContext)

    suspend fun getGoogleIdToken(): GoogleCredentialResult = try {
        val googleIdOption = GetSignInWithGoogleOption.Builder(
            activityContext.getString(R.string.default_web_client_id),
        ).build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        val credential = credentialManager.getCredential(activityContext, request).credential

        if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
            GoogleCredentialResult.Success(googleCredential.idToken)
        } else {
            GoogleCredentialResult.Failure
        }
    } catch (exception: GetCredentialCancellationException) {
        Log.d(TAG, "Google credential request was cancelled", exception)
        GoogleCredentialResult.Cancelled
    } catch (exception: Exception) {
        Log.e(TAG, "Failed to get Google credential", exception)
        GoogleCredentialResult.Failure
    }

    private companion object {
        const val TAG = "GoogleCredential"
    }
}
