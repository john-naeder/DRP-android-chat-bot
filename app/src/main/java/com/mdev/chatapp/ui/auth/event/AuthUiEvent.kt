package com.mdev.chatapp.ui.auth.event

sealed class AuthUiEvent {
    data class UsernameChanged(val value: String): AuthUiEvent()
    data class PasswordChanged(val value: String): AuthUiEvent()
    data class RePasswordChanged(val value: String) : AuthUiEvent()
    data class EmailChanged(val value: String): AuthUiEvent()
    data class SignedInUsernameChanged(val value: String): AuthUiEvent()
    data class DeleteUserChanged(val value: String): AuthUiEvent()
    data class UnAuthenticatedUserChanged(val username: String): AuthUiEvent()
    data object SignIn: AuthUiEvent()
    data object SignUp: AuthUiEvent()
    data object Authenticated: AuthUiEvent()
    data object SignedIn: AuthUiEvent()

}