package com.mdev.chatapp.domain.repository

import com.mdev.chatapp.data.remote.auth.ApiResult

interface AuthRepository {
    suspend fun signUp(username: String, password: String, email: String): ApiResult<Unit>
    suspend fun signIn(username: String, password: String): ApiResult<Unit>
    suspend fun logout(): ApiResult<Unit>
    suspend fun authenticate(): ApiResult<Unit>
    suspend fun refreshToken(username: String): ApiResult<Unit>
    suspend fun unauthenticated(username: String): ApiResult<Unit>
    suspend fun testConnection(): Boolean
    suspend fun authenticateSignedUser(username: String): ApiResult<Unit>
}