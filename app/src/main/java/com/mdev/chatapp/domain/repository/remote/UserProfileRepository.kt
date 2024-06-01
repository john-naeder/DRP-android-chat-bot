package com.mdev.chatapp.domain.repository.remote

import com.mdev.chatapp.data.remote.user.model.UserProfileResponse
import com.mdev.chatapp.domain.result.ApiResult

interface UserProfileRepository {
    suspend fun getUserInfo(username: String) : ApiResult<UserProfileResponse>
    suspend fun updateUserInfo(
        dateOfBirth: String,
        height: Float,
        name: String,
        username: String,
        weight: Float
    ): ApiResult<Unit>
}