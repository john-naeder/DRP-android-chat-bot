package com.mdev.chatapp.data.remote.auth

import com.mdev.chatapp.data.remote.auth.model.SignInRequest
import com.mdev.chatapp.data.remote.auth.model.SignUpRequest
import com.mdev.chatapp.domain.repository.AuthRepository
import com.mdev.chatapp.util.DataStoreHelper
import com.mdev.chatapp.util.Jwt
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val dataStore: DataStoreHelper
) : AuthRepository {

    override suspend fun signUp(
        username: String,
        password: String,
        email: String
    ): ApiResult<Unit> {
        return try {
            authApi.signUp(SignUpRequest(username, password, email))
            signIn(username, password)
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> ApiResult.Error("Username already exists")
                403 -> ApiResult.Error("Invalid credentials")
                404 -> ApiResult.Error("User not found")
                else -> ApiResult.Error(e.message())

            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Sign up error: " + e.message)
        }
    }

    override suspend fun signIn(username: String, password: String): ApiResult<Unit> {
        return try {
            val response = authApi.signIn(request = SignInRequest(username, password))
            dataStore.setString("jwt$username", response.tokens.accessToken)
            dataStore.setString("jwt_refresh$username", response.tokens.refreshToken)
            dataStore.setString("current_user", username)
            ApiResult.Authorized()
        } catch(e: HttpException) {
            when(e.code()) {
                404 -> ApiResult.Error(
                    "User not found" + e.message() + " " + e.code() +
                            " " + e.response()?.errorBody()?.string()
                )
                else -> ApiResult.Error(e.message() + " " + e.code())
            }
        } catch(e: Exception) {
            ApiResult.UnknownError("Sign in error: " + e.message)
        }
    }

    override suspend fun logout(): ApiResult<Unit> {
        TODO()
    }

    override suspend fun authenticate(): ApiResult<Unit> {
        return try {
            val token = dataStore.getString("jwt"+ dataStore.getString("current_user")) ?: return ApiResult.Unauthorized()
            if(Jwt.isJWTExpired(token)) {
                refreshToken(dataStore.getString("current_user")!!)
            }
            authApi.authenticate("Bearer $token")
            ApiResult.Authorized()
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> ApiResult.Error("Invalid token")
                401 -> ApiResult.Error("Null token")
                else -> ApiResult.Error("Authentication failed: " + e.message())
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Auth error: " + e.message + e.stackTraceToString())
        }
    }

    override suspend fun authenticateSignedUser(username: String): ApiResult<Unit> {
        return try {
            val token = dataStore.getString("jwt$username") ?: return ApiResult.Unauthorized()
            if(Jwt.isJWTExpired(token)) {
                refreshToken(username)
            }
            authApi.authenticate("Bearer $token")
            ApiResult.Authorized()
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> ApiResult.Error("Invalid token")
                401 -> ApiResult.Error("Null token")
                else -> ApiResult.Error("Authentication failed: " + e.message())
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Auth error: " + e.message + e.stackTraceToString())
        }
    }

    override suspend fun unauthenticated(username: String): ApiResult<Unit> {
        dataStore.remove("jwt$username")
        return ApiResult.Unauthorized()
    }

    override suspend fun testConnection(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun refreshToken(username: String): ApiResult<Unit> {
        return try {
            val token = dataStore.getString("jwt_refresh$username") ?: return ApiResult.Error("No refresh token")
            val response = authApi.refreshToken("Bearer $token")
            dataStore.setString("jwt"+ dataStore.getString("current_user"), response.accessToken)
            ApiResult.Authorized()
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> ApiResult.Error("Invalid token or token expired")
                401 -> ApiResult.Error("Null token")
                else -> ApiResult.Error("Authentication failed: " + e.message())
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Auth error: " + e.message + e.stackTraceToString())
        }
    }

}