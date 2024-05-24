package com.mdev.chatapp.data.local.acccount

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserModel(
    @PrimaryKey
    val id: String,
    val email: String,
    val username: String
)