package com.mdev.chatapp.data.local.user

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mdev.chatapp.util.Constants

@Database(entities = [AccountModel::class], version = 3)
abstract class AccountDatabase : RoomDatabase(){
    abstract val dao: AccountDao

    companion object {
        const val DATABASE_NAME: String = Constants.ACCOUNT_DATABASE_NAME
    }

}