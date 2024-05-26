package com.mdev.chatapp.data.local.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userModel: UserModel)

    @Delete
    suspend fun deleteUser(userModel: UserModel)

    @Query("DELETE FROM userModel WHERE username = :username")
    suspend fun deleteUserById(username: String)

    @Query("SELECT * FROM userModel")
    fun getAllUsers(): Flow<List<UserModel>>

    @Query("SELECT * FROM userModel WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserModel?

    @Query("SELECT * FROM userModel WHERE id = :id")
    suspend fun getUserById(id: String): UserModel?

}