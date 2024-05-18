package com.mdev.chatapp.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.data.local.user.UserSignedInModel
import com.mdev.chatapp.data.remote.auth.ApiResult
import com.mdev.chatapp.domain.repository.AuthRepository
import com.mdev.chatapp.domain.repository.UserSignedInRepository
import com.mdev.chatapp.domain.user_entry.AuthenticateUseCase
import com.mdev.chatapp.ui.auth.event_state.AuthState
import com.mdev.chatapp.ui.auth.event_state.AuthUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authenticateUseCase: AuthenticateUseCase,
    private val authRepository: AuthRepository,
    private val userSignedInRepository: UserSignedInRepository
): ViewModel(){
    var state by mutableStateOf(AuthState())
    private val resultChannel = Channel<ApiResult<Unit>>()
    val result = resultChannel.receiveAsFlow()

    init {
        authenticate()
    }

    fun onEvent(event: AuthUiEvent) {
        when(event) {
            is AuthUiEvent.SignUpUsernameChanged -> {
                state = state.copy(signUpUsername = event.value)
            }
            is AuthUiEvent.SignUpPasswordChanged -> {
                state = state.copy(signUpPassword = event.value)
            }
            is AuthUiEvent.SignUpRePasswordChanged -> {
                state = state.copy(signUpRePassword = event.value)
            }
            is AuthUiEvent.SignUpEmailChanged -> {
                state = state.copy(signUpEmail = event.value)
            }
            is AuthUiEvent.SignUp -> {
                signUp()
            }
            is AuthUiEvent.SignInUsernameChanged -> {
                state = state.copy(signInUsername = event.value)
            }
            is AuthUiEvent.SignInPasswordChanged -> {
                state = state.copy(signInPassword = event.value)
            }
            is AuthUiEvent.SignIn -> {
                signIn()
            }
            is AuthUiEvent.Logout -> {
                logout()
            }
            is AuthUiEvent.SignedInUsernameChanged -> {
                state = state.copy(signedInUsernameChanged = event.value)
                authenticateSignedUser()
            }
        }
    }
    private fun signUp(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = authRepository.signUp(
                state.signUpUsername,
                state.signUpPassword,
                state.signUpEmail
            )
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }
    private fun signIn(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = authRepository.signIn(
                state.signInUsername,
                state.signInPassword
            )
            userSignedInRepository.insertUser(UserSignedInModel(state.signInUsername))
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }
    private fun authenticate(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = authenticateUseCase()
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }
    private fun logout (){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = authRepository.logout()
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }
    private fun authenticateSignedUser(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = authRepository.authenticateSignedUser(state.signedInUsernameChanged)
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }
}

