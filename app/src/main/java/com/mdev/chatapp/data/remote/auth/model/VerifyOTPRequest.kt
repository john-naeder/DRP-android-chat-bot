package com.mdev.chatapp.data.remote.auth.model

data class VerifyOTPRequest (
    val email: String,
    val otp: String
)