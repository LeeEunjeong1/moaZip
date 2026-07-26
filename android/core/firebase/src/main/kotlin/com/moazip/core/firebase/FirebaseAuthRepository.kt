package com.moazip.core.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.moazip.core.domain.repository.AuthRepository
import com.moazip.core.domain.repository.UserRepository
import com.moazip.core.model.UserProfile
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserRepository,
) : AuthRepository {
    override val isAuthenticated: Boolean
        get() = firebaseAuth.currentUser != null

    override suspend fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val firebaseUser = requireNotNull(
            firebaseAuth.signInWithCredential(credential).await().user,
        ) {
            "Firebase authentication succeeded without a user."
        }

        userRepository.createIfAbsent(
            UserProfile(
                uid = firebaseUser.uid,
                email = firebaseUser.email,
                displayName = firebaseUser.displayName,
                photoUrl = firebaseUser.photoUrl?.toString(),
            ),
        )
    }
}
