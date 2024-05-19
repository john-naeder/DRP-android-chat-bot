package com.mdev.chatapp.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdev.chatapp.data.local.user.UserSignedInModel
import com.mdev.chatapp.ui.auth.event_state.AuthResult
import com.mdev.chatapp.domain.repository.AuthRepository
import com.mdev.chatapp.domain.repository.UserSignedInRepository
import com.mdev.chatapp.ui.auth.event_state.AuthState
import com.mdev.chatapp.ui.auth.event_state.AuthUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userSignedInRepository: UserSignedInRepository
): ViewModel(){

    var state by mutableStateOf(AuthState())
    private val uiEventChannel = Channel<AuthResult<Unit>>()
    val uiEvent = uiEventChannel.receiveAsFlow()
    val users = userSignedInRepository.getAllUser()

    fun onEvent(event: AuthUiEvent) {
        when(event) {
            is AuthUiEvent.UsernameChanged -> {
                state = state.copy(username = event.value)
            }
            is AuthUiEvent.PasswordChanged -> {
                state = state.copy(password = event.value)
            }
            is AuthUiEvent.RePasswordChanged -> {
                state = state.copy(rePassword = event.value)
            }
            is AuthUiEvent.EmailChanged -> {
                state = state.copy(email = event.value)
            }
            is AuthUiEvent.SignUp -> {
                signUp()
            }

            is AuthUiEvent.SignIn -> {
                signIn()
            }
            is AuthUiEvent.SignedInUsernameChanged -> {
                state = state.copy(signedInUsernameChanged = event.value)
            }
            is AuthUiEvent.DeleteUserChanged -> {
                state = state.copy(signedInUsernameChanged = event.value)
            }
            is AuthUiEvent.SignedIn -> {
                authenticateSignedUser()
            }
            is AuthUiEvent.Authenticated -> {
                authenticate()
            }
            is AuthUiEvent.UnAuthenticatedUserChanged -> {
                unauthenticatedUser()
            }
        }
    }
    private fun signUp(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = authRepository.signUp(
                state.username,
                state.password,
                state.email
            )
            userSignedInRepository.insertUser(UserSignedInModel(state.username))
            uiEventChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }
    private fun signIn(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = authRepository.signIn(
                state.username,
                state.password
            )
            userSignedInRepository.insertUser(UserSignedInModel(state.username))
            uiEventChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }
    fun authenticate(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = authRepository.authenticate()
            uiEventChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }
    private fun authenticateSignedUser(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = authRepository.authenticateSignedUser(state.signedInUsernameChanged)
            uiEventChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }

    private fun unauthenticatedUser(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = authRepository.unAuthenticateUser(state.signedInUsernameChanged)
            userSignedInRepository.deleteUserById(state.signedInUsernameChanged)
            uiEventChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }
}

