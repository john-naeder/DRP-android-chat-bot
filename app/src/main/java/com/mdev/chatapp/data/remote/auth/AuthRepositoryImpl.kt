package com.mdev.chatapp.data.remote.auth

import com.mdev.chatapp.data.remote.auth.model.SignInRequest
import com.mdev.chatapp.data.remote.auth.model.SignUpRequest
import com.mdev.chatapp.domain.repository.AuthRepository
import com.mdev.chatapp.ui.auth.event_state.AuthResult
import com.mdev.chatapp.util.DataStoreHelper
import com.mdev.chatapp.util.Jwt
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val dataStore: DataStoreHelper
) : AuthRepository {

    private val JWT = "jwt_"
    private val JWT_REFRESH = "jwt_refresh_"
    private val CURRENT_USER = "current_user"
    override suspend fun signUp(
        username: String,
        password: String,
        email: String
    ): AuthResult<Unit> {
        return try {
            authApi.signUp(SignUpRequest(username, password, email))
            signIn(username, password)
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> AuthResult.Error("Username already exists")
                403 -> AuthResult.Error("Invalid credentials")
                404 -> AuthResult.Error("User not found")
                else -> AuthResult.Error(e.message())

            }
        } catch (e: Exception) {
            AuthResult.UnknownError("Sign up error: " + e.message)
        }
    }

    override suspend fun signIn(username: String, password: String): AuthResult<Unit> {
        return try {
            val response = authApi.signIn(request = SignInRequest(username, password))

            dataStore.setString(JWT + username, response.tokens.accessToken)
            dataStore.setString(JWT_REFRESH + username, response.tokens.refreshToken)
            dataStore.setString(JWT + CURRENT_USER, response.tokens.accessToken)
            dataStore.setString(CURRENT_USER, username)

            AuthResult.Authorized()
        } catch(e: HttpException) {
            when(e.code()) {
                404 -> AuthResult.Error(
                    "User not found" + e.message() + " " + e.code() +
                            " " + e.response()?.errorBody()?.string()
                )
                else -> AuthResult.Error(e.message() + " " + e.code())
            }
        } catch(e: Exception) {
            AuthResult.UnknownError("Sign in error: " + e.message)
        }
    }



    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = dataStore.getString(JWT + CURRENT_USER)
                ?: return AuthResult.Error("Authentication failed: No current login user found")

            if(Jwt.isJWTExpired(token)) {
                return refreshToken(dataStore.getString(CURRENT_USER)!!)
            }
            authApi.authenticate("Bearer $token")
            AuthResult.Authorized()
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> AuthResult.Error("Invalid token")
                401 -> AuthResult.Error("Null token")
                else -> AuthResult.Error("Authentication failed: " + e.message())
            }
        } catch (e: Exception) {
            AuthResult.UnknownError("Auth error: " + e.message + e.stackTraceToString())
        }
    }

    override suspend fun authenticateSignedUser(username: String): AuthResult<Unit> {
        return try {
            val token = dataStore.getString("jwt_$username") ?:
                return AuthResult.Error("Authentication failed: Token not found")

            if(Jwt.isJWTExpired(token)) {
                return refreshToken(username)
            }
            dataStore.setString(JWT + CURRENT_USER, token)
            AuthResult.Authorized()
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> AuthResult.Error("Invalid token")
                401 -> AuthResult.Error("Null token")
                else -> AuthResult.Error("Authentication failed: " + e.message())
            }
        } catch (e: Exception) {
            AuthResult.UnknownError("Auth error: " + e.message + e.stackTraceToString())
        }
    }
    override suspend fun refreshToken(username: String): AuthResult<Unit> {
        return try {
            val token = dataStore.getString(JWT_REFRESH +  username)
                ?: return AuthResult.Error("Refresh token failed: No refresh token found")

            val response = authApi.refreshToken("Bearer $token")
            dataStore.setString(JWT + username, response.accessToken)
            dataStore.setString(JWT + CURRENT_USER, response.accessToken)
            authenticateSignedUser(username)
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> AuthResult.Error("Invalid token")
                401 -> AuthResult.Error("Null token")
                else -> AuthResult.Error("Authentication failed: " + e.message())
            }
        } catch (e: Exception) {
            AuthResult.UnknownError("Refresh Auth error: " + e.message + e.stackTraceToString())
        }
    }

    override suspend fun unAuthenticateUser(username: String): AuthResult<Unit> {
        return try {
            dataStore.remove(JWT + username)
            dataStore.remove(JWT_REFRESH + username)
            AuthResult.Unauthorized("User $username unauthenticated")
        } catch (e: Exception) {
            AuthResult.UnknownError("Auth error: " + e.message + e.stackTraceToString())
        }
    }

    override suspend fun testConnection(): Boolean {
        TODO("Not yet implemented")
    }
}