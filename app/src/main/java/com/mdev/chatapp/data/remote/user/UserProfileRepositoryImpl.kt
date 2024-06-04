package com.mdev.chatapp.data.remote.user

import com.mdev.chatapp.data.remote.api.UserServerApi
import com.mdev.chatapp.data.remote.user.model.UpdateUserProfileRequest
import com.mdev.chatapp.data.remote.user.model.UserProfileRequest
import com.mdev.chatapp.data.remote.user.model.UserProfileResponse
import com.mdev.chatapp.domain.repository.remote.UserProfileRepository
import com.mdev.chatapp.domain.result.ApiResult

class UserProfileRepositoryImpl(
    private val userProfileApi: UserServerApi
) : UserProfileRepository {

    override suspend fun getUserInfo(username: String): ApiResult<UserProfileResponse> {
        return try {
            val response = userProfileApi.getUserInfo(UserProfileRequest(username))
            when (response.code()) {
                200 -> {
                    ApiResult.Success(response.body()!!)
                }
                400 -> {
                    ApiResult.LogError("User deleted")
                }
                404 -> {
                    ApiResult.LogError("User not found")
                }
                else -> {
                    ApiResult.LogError("Get user info failed: Unknown error ${response.code()} ${response.message()}")
                }
            }
        } catch (e: Throwable) {
            ApiResult.UnknownError("Get user info failed: ${e.message} ${e.localizedMessage}")
        }
    }


    override suspend fun updateUserInfo(
        dateOfBirth: String,
        height: Float,
        name: String,
        username: String,
        weight: Float
    ): ApiResult<Unit> {
        return try {
            val response = userProfileApi.updateUserInfo(
                UpdateUserProfileRequest(
                    dateOfBirth = dateOfBirth,
                    height = height,
                    name = name,
                    username = username,
                    weight = weight
                )
            )
            when (response.code()) {
                201 -> {
                    ApiResult.Success()
                }

                400 -> {
                    ApiResult.LogError("Bad request: body error")
                }

                404 -> {
                    ApiResult.LogError("User not found")
                }

                else -> {
                    ApiResult.UnknownError("Update user info failed (API): Unknown error")
                }
            }
        } catch (e: Throwable) {
            ApiResult.UnknownError("Update user info failed: ${e.message} ${e.localizedMessage}")
        }
    }
}
