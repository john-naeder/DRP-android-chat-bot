package com.mdev.chatapp.domain.repository

import com.mdev.chatapp.data.local.user.UserSignedInModel
import kotlinx.coroutines.flow.Flow

interface UserSignedInRepository {
    suspend fun insertUser(userSignedInModel: UserSignedInModel)

    suspend fun deleteUser(userSignedInModel: UserSignedInModel)

    fun getUserById(id: String): UserSignedInModel?

    fun getAllUser(): Flow<List<UserSignedInModel>>
}