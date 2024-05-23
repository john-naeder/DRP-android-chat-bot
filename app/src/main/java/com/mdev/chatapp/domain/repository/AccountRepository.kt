package com.mdev.chatapp.domain.repository

import com.mdev.chatapp.data.local.user.AccountModel
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    suspend fun insertUser(accountModel: AccountModel)
    suspend fun deleteUser(accountModel: AccountModel)
    suspend fun deleteUserById(username: String)
    fun getUserById(id: String): AccountModel?
    fun getAllUser(): Flow<List<AccountModel>>

}