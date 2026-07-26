package com.moazip.core.firebase

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.moazip.core.domain.repository.UserRepository
import com.moazip.core.model.UserProfile
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository(
    private val firestore: FirebaseFirestore,
) : UserRepository {
    override suspend fun createIfAbsent(user: UserProfile) {
        val userDocument = firestore.collection(USERS_COLLECTION).document(user.uid)

        firestore.runTransaction { transaction ->
            if (!transaction.get(userDocument).exists()) {
                transaction.set(
                    userDocument,
                    mapOf(
                        UID_FIELD to user.uid,
                        EMAIL_FIELD to user.email,
                        DISPLAY_NAME_FIELD to user.displayName,
                        PHOTO_URL_FIELD to user.photoUrl,
                        PROVIDER_FIELD to GOOGLE_PROVIDER,
                        CREATED_AT_FIELD to FieldValue.serverTimestamp(),
                    ),
                )
            }
        }.await()
    }

    private companion object {
        const val USERS_COLLECTION = "users"
        const val UID_FIELD = "uid"
        const val EMAIL_FIELD = "email"
        const val DISPLAY_NAME_FIELD = "displayName"
        const val PHOTO_URL_FIELD = "photoUrl"
        const val PROVIDER_FIELD = "provider"
        const val CREATED_AT_FIELD = "createdAt"
        const val GOOGLE_PROVIDER = "google"
    }
}
