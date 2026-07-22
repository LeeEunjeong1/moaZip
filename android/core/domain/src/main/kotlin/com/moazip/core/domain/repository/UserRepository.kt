package com.moazip.core.domain.repository

import com.moazip.core.model.UserProfile

interface UserRepository {
    suspend fun createIfAbsent(user: UserProfile)
}
