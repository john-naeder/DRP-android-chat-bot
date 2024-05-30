package com.mdev.chatapp.ui.auth

import com.mdev.chatapp.R

data class AuthState(
    val isLoading: Boolean = false,

    val username: String = "",
    val usernameError: Boolean = false,
    val usernameErrorCode: Int = R.string.null_field,

    val password: String = "",
    val passwordError: Boolean = false,
    val passwordErrorCode: Int = R.string.required_field,

    val rePassword: String = "",
    val rePasswordError: Boolean = false,
    val rePasswordErrorCode:  Int = R.string.required_field,

    val email: String = "",
    val emailError: Boolean = false,
    val emailErrorCode: Int = R.string.required_field,

    val isVerifyOTP: Boolean = false,
    val isInputEmailOTP: Boolean = false,
    val isResetPassword: Boolean = false,

    val otp: String = "",
    val otpError: Boolean = false,
    val otpErrorCode: Int = R.string.required_field,

    val signedInUsernameChanged: String = ""
)