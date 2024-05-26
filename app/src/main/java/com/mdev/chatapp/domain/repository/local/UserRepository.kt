package com.mdev.chatapp.domain.repository.local

import com.mdev.chatapp.data.local.user.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun insertUser(userModel: UserModel)
    suspend fun deleteUser(userModel: UserModel)
    suspend fun deleteUserById(username: String)
    suspend fun getUserById(id: String): UserModel?
    suspend fun getUserByUsername(username: String): UserModel?
    fun getAllUser(): Flow<List<UserModel>>

}