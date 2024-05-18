package com.mdev.chatapp.data.local.user

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserSignedInModel::class], version = 3)
abstract class UserSignedInDatabase : RoomDatabase(){
    abstract val dao: UserSignedInDao  // Research more on this

    companion object {
        const val DATABASE_NAME: String = "user_signed_in_db"
    }

}