package com.moazip.app.auth.contract

sealed interface GoogleCredentialResult {
    data class Success(val idToken: String) : GoogleCredentialResult
    data object Cancelled : GoogleCredentialResult
    data object Failure : GoogleCredentialResult
}
