package com.mdev.chatapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mdev.chatapp.data.local.user.UserDao
import com.mdev.chatapp.data.local.user.UserModel
import com.mdev.chatapp.util.Constants

@Database(entities = [UserModel::class], version = 7)
abstract class LocalDatabase : RoomDatabase(){
    abstract val userDao: UserDao

    companion object {
        const val DATABASE_NAME: String = Constants.LOCAL_DATABASE_NAME
    }

}