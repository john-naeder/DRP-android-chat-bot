package com.mdev.chatapp.data.remote.auth

import com.mdev.chatapp.R
import com.mdev.chatapp.data.local.user.UserModel
import com.mdev.chatapp.data.remote.auth.model.SignInRequest
import com.mdev.chatapp.data.remote.auth.model.SignUpRequest
import com.mdev.chatapp.domain.repository.local.UserRepository
import com.mdev.chatapp.domain.repository.remote.AuthRepository
import com.mdev.chatapp.domain.result.ApiResult
import com.mdev.chatapp.util.Constants.CURRENT_USER
import com.mdev.chatapp.util.Constants.CURRENT_USER_ID
import com.mdev.chatapp.util.Constants.JWT
import com.mdev.chatapp.util.Constants.JWT_REFRESH
import com.mdev.chatapp.util.DataStoreHelper
import com.mdev.chatapp.util.Jwt
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val dataStore: DataStoreHelper,
    private val userRepository: UserRepository
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
                400 -> ApiResult.Error(R.string.username_existed)
                else -> ApiResult.UnknownError("Sign up error: "+ e.message())

            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Sign up error: " + e.message)
        }
    }

    override suspend fun signIn(username: String, password: String): ApiResult<Unit> {
        return try {
            val response = authApi.signIn(request = SignInRequest(username, password))
            val responseBody = if(response.isSuccessful) response.body()!!
            else return ApiResult.Error(R.string.user_not_found)

            responseBody.tokens.let {
                dataStore.setString(JWT + username, it.accessToken)
                dataStore.setString(JWT_REFRESH + username, it.refreshToken)

            }
            responseBody.account.let {
                dataStore.setString(CURRENT_USER, it.username)
                dataStore.setString(CURRENT_USER_ID, it.id)
            }

            userRepository.insertUser(
                UserModel(
                    id = responseBody.account.id,
                    username = responseBody.account.username,
                    email = responseBody.account.email,
                )
            )

            ApiResult.Success()
        } catch(e: HttpException) {
            when(e.code()) {
                404 -> ApiResult.Error(R.string.user_not_found)
                else -> ApiResult.UnknownError(e.message())
            }
        } catch(e: Exception) {
            ApiResult.UnknownError("Sign in error: " + e.message)
        }
    }

    override suspend fun authenticate(): ApiResult<Unit> {
        return try {
            val token = dataStore.getString(JWT + CURRENT_USER)
                ?: return ApiResult.Error(R.string.null_token_response)

            if(Jwt.isJWTExpired(token)) {
                return refreshToken(dataStore.getString(CURRENT_USER)!!)
            }
            authApi.authenticate("Bearer $token")
            return ApiResult.Success()

        } catch (e: HttpException) {
            when (e.code()) {
                400, 401  -> ApiResult.Error(R.string.invalid_token_or_token_not_found)
                else -> ApiResult.UnknownError("Authentication failed: " + e.message())
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Authentication error: " + e.message + e.stackTraceToString())
        }
    }

    override suspend fun authenticateSignedUser(username: String): ApiResult<Unit> {
        return try {
            val token = dataStore.getString("jwt_$username") ?:
                return ApiResult.Error(R.string.null_token_response)

            if(Jwt.isJWTExpired(token)) {
                return refreshToken(username)
            }

            dataStore.setString(CURRENT_USER, username)
            dataStore.setString(CURRENT_USER_ID, userRepository.getUserByUsername(username)!!.id)
            dataStore.setString(JWT + CURRENT_USER, token)
            ApiResult.Success()

        } catch (e: HttpException) {
            when (e.code()) {
                400, 401 -> ApiResult.Error(R.string.invalid_token_or_token_not_found)
                else -> ApiResult.UnknownError("Authentication failed: " + e.message())
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Authentication error: " + e.message + e.stackTraceToString())
        }
    }
    override suspend fun refreshToken(username: String): ApiResult<Unit> {
        return try {
            val token = dataStore.getString(JWT_REFRESH +  username)
                ?: return ApiResult.Error(R.string.null_token_response)

            val response = authApi.refreshToken("Bearer $token").body()
                ?: return ApiResult.Error(R.string.refresh_token_not_found_or_invalid)
            dataStore.setString(JWT + username, response.accessToken)
            dataStore.setString(JWT + CURRENT_USER, response.accessToken)
            authenticateSignedUser(username)
        } catch (e: HttpException) {
            when (e.code()) {
                400, 401 -> ApiResult.Error(R.string.invalid_token_or_token_not_found)
                else -> ApiResult.UnknownError("Authentication failed: " + e.message())
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Refresh Authentication error: " + e.message + e.stackTraceToString())
        }
    }

    override suspend fun unAuthenticateUser(username: String): ApiResult<Unit> {
        return try {
            dataStore.remove(JWT + username)
            dataStore.remove(JWT_REFRESH + username)
            ApiResult.Unauthorized("User $username unauthenticated")
        } catch (e: Exception) {
            ApiResult.UnknownError("Authentication error: " + e.message + e.stackTraceToString())
        }
    }

    override suspend fun logout() {
        dataStore.remove(JWT + CURRENT_USER)
        dataStore.remove(CURRENT_USER)
        dataStore.remove(CURRENT_USER_ID)
    }
}