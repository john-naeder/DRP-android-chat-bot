package com.mdev.chatapp.data.remote.auth

import com.mdev.chatapp.R
import com.mdev.chatapp.data.local.user.UserModel
import com.mdev.chatapp.data.remote.api.UserServerApi
import com.mdev.chatapp.data.remote.auth.model.ResetPasswordRequest
import com.mdev.chatapp.data.remote.auth.model.SendOTPRequest
import com.mdev.chatapp.data.remote.auth.model.SignInRequest
import com.mdev.chatapp.data.remote.auth.model.SignUpRequest
import com.mdev.chatapp.data.remote.auth.model.VerifyOTPRequest
import com.mdev.chatapp.domain.repository.local.UserRepository
import com.mdev.chatapp.domain.repository.remote.AuthRepository
import com.mdev.chatapp.domain.result.ApiResult
import com.mdev.chatapp.util.Constants.CURRENT_USER
import com.mdev.chatapp.util.Constants.CURRENT_USER_ID
import com.mdev.chatapp.util.Constants.JWT
import com.mdev.chatapp.util.Constants.JWT_REFRESH
import com.mdev.chatapp.util.DataStoreHelper
import com.mdev.chatapp.util.Util

class AuthRepositoryImpl(
    private val userServerApi: UserServerApi,
    private val dataStore: DataStoreHelper,
    private val userRepository: UserRepository
) : AuthRepository {
    override suspend fun signUp(username: String, password: String, email: String): ApiResult<Unit> {
        return try {
            val response = userServerApi.signUp(SignUpRequest(username, password, email))
            when (response.code()) {
                201 -> signIn(username, password)
                else -> ApiResult.UnknownError("Sign up error: " + response.message())
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Sign up error: " + e.message)
        }
    }

    override suspend fun signIn(username: String, password: String): ApiResult<Unit> {
        return try {
            val response = userServerApi.signIn(request = SignInRequest(username, password))
            when(response.code()){
                200 -> {
                    response.body()!!.tokens.let {
                        dataStore.setString(JWT + username, it.accessToken)
                        dataStore.setString(JWT_REFRESH + username, it.refreshToken)
                    }
                    response.body()!!.account.let {
                        dataStore.setString(CURRENT_USER, it.username)
                        dataStore.setString(CURRENT_USER_ID, it.id)
                    }

                    userRepository.insertUser(
                        UserModel(
                            id = response.body()!!.account.id,
                            username = response.body()!!.account.username,
                            email = response.body()!!.account.email,
                        )
                    )
                    ApiResult.Success()
                }
                else -> ApiResult.UnknownError("Sign in error: " + response.message())
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Sign in error: " + e.message)
        }
    }

    override suspend fun authenticate(): ApiResult<Unit> {
        return try {
            val token = dataStore.getString(JWT + CURRENT_USER)
                ?: return ApiResult.LogError("Token not found")

            if (Util.isJWTExpired(token)) {
                return refreshToken(dataStore.getString(CURRENT_USER)!!)
            }
            val response = userServerApi.authenticate("Bearer $token")

            when (response.code()) {
                200 -> {
                    ApiResult.Success()
                }

                400, 401 -> ApiResult.LogError("Invalid token or token not found")
                else -> ApiResult.UnknownError("Authentication failed: " + response.message())
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Authentication error: " + e.message + e.stackTraceToString())
        }
    }

    override suspend fun authenticateSignedUser(username: String): ApiResult<Unit> {
        return try {
            val token = dataStore.getString(JWT + username)
                ?: return ApiResult.LogError("Token not found")

            if (Util.isJWTExpired(token)) {
                return refreshToken(username)
            }
            val response = userServerApi.authenticate("Bearer $token")
            when (response.code()) {
                200 -> {
                    dataStore.setString(CURRENT_USER, username)
                    dataStore.setString(CURRENT_USER_ID, userRepository.getUserByUsername(username)!!.id)
                    dataStore.setString(JWT + CURRENT_USER, token)
                    ApiResult.Success()
                }
                400, 401 -> ApiResult.Error(R.string.invalid_token_or_token_not_found)
                else -> ApiResult.UnknownError("Authentication failed: " + response.message())
            }
        }  catch (e: Exception) {
            ApiResult.UnknownError("Authentication error: " + e.message + e.stackTraceToString())
        }
    }

    override suspend fun refreshToken(username: String): ApiResult<Unit> {
        return try {
            val token = dataStore.getString(JWT_REFRESH + username)
                ?: return ApiResult.LogError("Token not found")

            val response = userServerApi.refreshToken("Bearer $token")
            when (response.code()) {
                200 -> {
                    dataStore.setString(JWT + username, response.body()!!.accessToken)
                    dataStore.setString(JWT + CURRENT_USER, response.body()!!.accessToken)
                    authenticateSignedUser(username)
                }
                400, 401, 404 -> {
                    userRepository.deleteUserById(username)
                    ApiResult.Error(R.string.invalid_token_or_token_not_found)
                }
                else -> ApiResult.UnknownError("An unknown error occurred!")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Refresh Authentication error: " + e.message + e.stackTraceToString())
        }
    }

    override suspend fun unAuthenticateUser(username: String): ApiResult<Unit> {
        return try {
            dataStore.remove(JWT + username)
            dataStore.remove(JWT_REFRESH + username)
            ApiResult.UnknownError("Unauthenticated user: $username")
        } catch (e: Exception) {
            ApiResult.UnknownError("Authentication error: " + e.message + e.stackTraceToString())
        }
    }

    override suspend fun logout() {
        dataStore.remove(JWT + CURRENT_USER)
        dataStore.remove(CURRENT_USER)
        dataStore.remove(CURRENT_USER_ID)
    }

    override suspend fun sendOTP(email: String): ApiResult<Unit> {
        return try {
            val response = userServerApi.sendOTP(SendOTPRequest(email))
            when(response.code()){
                200 -> ApiResult.Success()
                400 -> ApiResult.Error(R.string.email_already_registered)
                500 -> ApiResult.Error(R.string.server_error)
                else -> ApiResult.UnknownError("Send OTP error: " + response.message())
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Send OTP error: " + e.message)
        }
    }

    override suspend fun verifyOTP(email: String, otp: String): ApiResult<Unit> {
        return try {
            val response = userServerApi.verifyOTP(VerifyOTPRequest(email, otp))
            when (response.code()) {
                200 -> ApiResult.Success()
                400 -> ApiResult.Error(R.string.invalid_otp)
                500 -> ApiResult.Error(R.string.server_error)
                else -> ApiResult.UnknownError("Verify OTP error: " + response.message())
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Verify OTP error: " + e.message)
        }
    }

    override suspend fun resetPasswordOTP(email: String): ApiResult<Unit> {
        return try {
            val response = userServerApi.resetPasswordOTP(SendOTPRequest(email))
            when (response.code()) {
                200 -> ApiResult.Success()
                400 -> ApiResult.Error(R.string.email_not_found)
                500 -> ApiResult.Error(R.string.server_error)
                else -> ApiResult.UnknownError("Reset password OTP (API error): " + response.message())
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Reset password error: " + e.message)
        }
    }

    override suspend fun resetPassword(
        email: String,
        password: String
    ): ApiResult<Unit> {
        return try {
            val response = userServerApi.resetPassword(ResetPasswordRequest(email, password))
            when (response.code()) {
                200 -> ApiResult.Success()
                400 -> ApiResult.Error(R.string.invalid_otp)
                500 -> ApiResult.Error(R.string.server_error)
                else -> ApiResult.UnknownError("Reset password error: " + response.message())
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Reset password OTP error: " + e.message)
        }
    }
}