package com.moazip.feature.auth.contract

sealed interface GoogleLoginOutcome {
    data object Success : GoogleLoginOutcome
    data object Cancelled : GoogleLoginOutcome
    data object Failure : GoogleLoginOutcome
}
