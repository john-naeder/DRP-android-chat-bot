package com.mdev.chatapp.data.local.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSignedInDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userSignedInModel: UserSignedInModel)

    @Delete
    suspend fun deleteUser(userSignedInModel: UserSignedInModel)

    @Query("SELECT * FROM userSignedInModel")
    fun getAllUsers(): Flow<List<UserSignedInModel>>

    @Query("SELECT * FROM userSignedInModel WHERE username = :username")
    fun getUserById(username: String): UserSignedInModel?

}