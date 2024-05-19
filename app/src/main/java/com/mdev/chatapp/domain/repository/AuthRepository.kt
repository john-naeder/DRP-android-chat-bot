package com.mdev.chatapp.domain.repository

import com.mdev.chatapp.ui.auth.event_state.AuthResult

interface AuthRepository {
    suspend fun signUp(username: String, password: String, email: String): AuthResult<Unit>
    suspend fun signIn(username: String, password: String): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
    suspend fun refreshToken(username: String): AuthResult<Unit>
    suspend fun testConnection(): Boolean
    suspend fun authenticateSignedUser(username: String): AuthResult<Unit>
    suspend fun unAuthenticateUser(username: String): AuthResult<Unit>
}