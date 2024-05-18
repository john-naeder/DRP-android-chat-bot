package com.mdev.chatapp.domain.user_entry

import com.mdev.chatapp.data.remote.auth.ApiResult
import com.mdev.chatapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthenticateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): ApiResult<Unit> {
        return authRepository.authenticate()
    }
}
