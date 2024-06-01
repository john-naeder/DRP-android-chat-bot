package com.mdev.chatapp.data.remote.user.model

data class UpdateUserProfileRequest(
    val dateOfBirth: String,
    val height: Float,
    val name: String,
    val username: String,
    val weight: Float
)