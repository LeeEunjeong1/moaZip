package com.moazip.core.firebase

import com.google.firebase.auth.FirebaseAuth
import com.moazip.core.domain.auth.CurrentUserProvider

class FirebaseCurrentUserProvider(
    private val firebaseAuth: FirebaseAuth,
) : CurrentUserProvider {
    override val userId: String?
        get() = firebaseAuth.currentUser?.uid

    override val displayName: String?
        get() = firebaseAuth.currentUser?.displayName
}
