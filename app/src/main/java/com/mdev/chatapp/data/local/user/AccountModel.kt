package com.mdev.chatapp.data.local.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AccountModel(
    @PrimaryKey
    val id: String,
    val email: String,
    val username: String
)