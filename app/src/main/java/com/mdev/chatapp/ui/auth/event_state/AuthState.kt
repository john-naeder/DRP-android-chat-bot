package com.mdev.chatapp.ui.auth.event_state

data class AuthState(
    val isLoading: Boolean = false,
    val signUpUsername: String = "",
    val signUpPassword: String = "",
    val signUpRePassword: String = "",
    val signInUsername: String = "",
    val signInPassword: String = "",
    val signUpEmail: String = "",
    val signedInUsernameChanged: String = ""
)