package com.mdev.chatapp.data.local.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(accountModel: AccountModel)

    @Delete
    suspend fun deleteUser(accountModel: AccountModel)

    @Query("DELETE FROM accountModel WHERE username = :username")
    suspend fun deleteUserById(username: String)

    @Query("SELECT * FROM accountModel")
    fun getAllUsers(): Flow<List<AccountModel>>

    @Query("SELECT * FROM accountModel WHERE username = :username")
    fun getUserByUsername(username: String): AccountModel?

    @Query("SELECT * FROM accountModel WHERE id = :id")
    fun getUserById(id: String): AccountModel?

}