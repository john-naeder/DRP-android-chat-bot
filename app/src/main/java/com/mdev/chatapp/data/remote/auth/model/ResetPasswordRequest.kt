package com.mdev.chatapp.data.remote.auth.model

data class ResetPasswordRequest(
    val email: String,
    val password: String
)