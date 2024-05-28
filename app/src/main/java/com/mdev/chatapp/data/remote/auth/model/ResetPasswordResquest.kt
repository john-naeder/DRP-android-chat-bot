package com.mdev.chatapp.data.remote.auth.model

data class ResetPasswordResquest (
    val email: String,
    val otp: String,
    val password: String
)