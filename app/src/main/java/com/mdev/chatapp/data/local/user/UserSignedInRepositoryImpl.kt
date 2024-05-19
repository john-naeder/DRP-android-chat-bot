package com.mdev.chatapp.data.local.user

import com.mdev.chatapp.domain.repository.UserSignedInRepository
import kotlinx.coroutines.flow.Flow

class UserSignedInRepositoryImpl(private val userSignedInDao: UserSignedInDao) : UserSignedInRepository {
    override suspend fun insertUser(userSignedInModel: UserSignedInModel) {
        userSignedInDao.insertUser(userSignedInModel)
    }
    override suspend fun deleteUser(userSignedInModel: UserSignedInModel) {
        userSignedInDao.deleteUser(userSignedInModel)
    }
    override fun getUserById(id: String): UserSignedInModel? {
        return userSignedInDao.getUserById(id)
    }

    override fun getAllUser(): Flow<List<UserSignedInModel>> {
        return userSignedInDao.getAllUsers()
    }
    override suspend fun deleteUserById(username: String) {
        userSignedInDao.deleteUserById(username)
    }
}