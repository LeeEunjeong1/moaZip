package com.moazip.core.domain.repository

interface AuthRepository {
    val isAuthenticated: Boolean

    suspend fun signInWithGoogle(idToken: String)

    fun signOut()
}
