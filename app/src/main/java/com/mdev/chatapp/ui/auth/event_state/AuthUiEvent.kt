package com.mdev.chatapp.ui.auth.event_state

sealed class AuthUiEvent {
    data class SignUpUsernameChanged(val value: String): AuthUiEvent()
    data class SignUpPasswordChanged(val value: String): AuthUiEvent()
    data class SignUpRePasswordChanged(val value: String) : AuthUiEvent()
    data class SignUpEmailChanged(val value: String): AuthUiEvent()
    data object SignUp: AuthUiEvent()

    data class SignInUsernameChanged(val value: String): AuthUiEvent()
    data class SignInPasswordChanged(val value: String): AuthUiEvent()
    data object SignIn: AuthUiEvent()

    data object Logout: AuthUiEvent()

    data class SignedInUsernameChanged(val value: String): AuthUiEvent()
}