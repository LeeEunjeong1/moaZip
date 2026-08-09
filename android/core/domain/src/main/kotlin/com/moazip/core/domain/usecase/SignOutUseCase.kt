package com.moazip.core.domain.usecase

import com.moazip.core.domain.repository.AuthRepository

class SignOutUseCase(
    private val repository: AuthRepository,
) {
    operator fun invoke() = repository.signOut()
}
