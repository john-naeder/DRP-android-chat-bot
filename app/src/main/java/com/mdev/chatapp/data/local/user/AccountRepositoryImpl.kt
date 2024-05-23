package com.mdev.chatapp.data.local.user

import com.mdev.chatapp.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class AccountRepositoryImpl(private val accountDao: AccountDao) : AccountRepository {
    override suspend fun insertUser(accountModel: AccountModel) {
        accountDao.insertUser(accountModel)
    }
    override suspend fun deleteUser(accountModel: AccountModel) {
        accountDao.deleteUser(accountModel)
    }
    override fun getUserById(id: String): AccountModel? {
        return accountDao.getUserByUsername(id)
    }

    override fun getAllUser(): Flow<List<AccountModel>> {
        return accountDao.getAllUsers()
    }
    override suspend fun deleteUserById(username: String) {
        accountDao.deleteUserById(username)
    }
}