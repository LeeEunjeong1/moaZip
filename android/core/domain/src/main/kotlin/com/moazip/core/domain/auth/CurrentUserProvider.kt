package com.moazip.core.domain.auth

interface CurrentUserProvider {
    val userId: String?
    val displayName: String?
}
