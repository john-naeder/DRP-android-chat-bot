package com.mdev.chatapp.data.local.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserSignedInModel(
    @PrimaryKey
    val username: String
)