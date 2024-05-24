package com.mdev.chatapp.data.local.acccount

import com.mdev.chatapp.domain.repository.local.UserRepository
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {
    override suspend fun insertUser(userModel: UserModel) {
        userDao.insertUser(userModel)
    }
    override suspend fun deleteUser(userModel: UserModel) {
        userDao.deleteUser(userModel)
    }
    override fun getUserById(id: String): UserModel? {
        return userDao.getUserByUsername(id)
    }

    override fun getAllUser(): Flow<List<UserModel>> {
        return userDao.getAllUsers()
    }
    override suspend fun deleteUserById(username: String) {
        userDao.deleteUserById(username)
    }
}