package com.mdev.chatapp.domain.repository.remote

import com.mdev.chatapp.domain.result.ApiResult

interface AuthRepository {
    suspend fun signUp(username: String, password: String, email: String): ApiResult<Unit>
    suspend fun signIn(username: String, password: String): ApiResult<Unit>
    suspend fun authenticate(): ApiResult<Unit>
    suspend fun refreshToken(username: String): ApiResult<Unit>
    suspend fun authenticateSignedUser(username: String): ApiResult<Unit>
    suspend fun unAuthenticateUser(username: String): ApiResult<Unit>
    suspend fun logout()
    suspend fun sendOTP(email: String): ApiResult<Unit>
    suspend fun verifyOTP(email: String, otp: String): ApiResult<Unit>
    suspend fun resetPasswordOTP(email: String): ApiResult<Unit>
    suspend fun resetPassword(email: String, password: String): ApiResult<Unit>


}