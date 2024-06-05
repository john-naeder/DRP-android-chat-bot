package com.mdev.chatapp.ui.auth.viewmode

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.R
import com.mdev.chatapp.domain.repository.remote.AuthRepository
import com.mdev.chatapp.domain.result.ApiResult
import com.mdev.chatapp.ui.auth.AuthState
import com.mdev.chatapp.ui.auth.AuthUiEvent
import com.mdev.chatapp.util.ValidateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel(), AuthViewModelInterface {

    override var state by mutableStateOf(AuthState())
    private val uiEventChannel = Channel<ApiResult<Unit>>()
    val uiEvent = uiEventChannel.receiveAsFlow()

    override fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.UsernameChanged -> {
                val usernameError = event.value.isEmpty() || !ValidateUtil.isValidUsername(event.value)
                val usernameErrorCode =
                    if (usernameError) {
                        when {
                            event.value.isEmpty() -> R.string.field_empty_error
                            !ValidateUtil.isValidUsername(event.value) -> R.string.username_invalid_error
                            else -> R.string.null_field
                        }
                    } else {
                        R.string.null_field
                    }
                state = state.copy(
                    username = event.value,
                    usernameError = usernameError,
                    usernameErrorCode = usernameErrorCode
                )
            }

            is AuthUiEvent.PasswordChanged -> {
                val passwordError = !ValidateUtil.isPasswordValid(event.value)
                val passwordErrorCode = when {
                    event.value.isEmpty() -> R.string.field_empty_error
                    event.value.length < 6 -> R.string.password_length_error
                    !event.value.matches(Regex(".*[^a-zA-Z0-9 ].*")) -> R.string.password_contain_error
                    else -> R.string.null_field
                }
                state = state.copy(
                    password = event.value,
                    passwordError = passwordError,
                    passwordErrorCode = passwordErrorCode
                )
            }

            is AuthUiEvent.RePasswordChanged -> {
                val rePasswordError = !ValidateUtil.isPasswordValid(event.value) || (state.password != event.value)

                val rePasswordErrorCode = when {
                    state.password != event.value -> R.string.password_not_match_error
                    else -> R.string.null_field
                }
                state = state.copy(
                    rePassword = event.value,
                    rePasswordError = rePasswordError,
                    rePasswordErrorCode = rePasswordErrorCode
                )
            }

            is AuthUiEvent.EmailChanged -> {
                val emailError = event.value.isEmpty() || !ValidateUtil.isEmailValid(event.value)
                val emailErrorCode = if (event.value.isEmpty()) R.string.field_empty_error
                    else if (!ValidateUtil.isEmailValid(event.value)) R.string.email_invalid_error
                    else R.string.null_field
                state = state.copy(
                    email = event.value, emailError = emailError, emailErrorCode = emailErrorCode
                )
            }

            is AuthUiEvent.OTPChanged -> {
                val otpError = event.value.isEmpty()
                val otpErrorCode = if (event.value.isEmpty()) R.string.field_empty_error
                else R.string.null_field
                state = state.copy(
                    otp = event.value, otpError = otpError, otpErrorCode = otpErrorCode
                )
            }

            is AuthUiEvent.SendOTP -> {
                sendOTP()
            }

            is AuthUiEvent.SignUp -> {
                signUp()
            }
            is AuthUiEvent.ResetState -> {
                state = AuthState()
            }
            is AuthUiEvent.BackToSignUpClick -> {
                state = state.copy(isVerifyOTP = false)
            }
            else -> {
                // do nothing
            }
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val otpResult = authRepository.verifyOTP(
                email = state.email, otp = state.otp
            )

            if (otpResult is ApiResult.Error) {
                uiEventChannel.send(otpResult)
                state = state.copy(isLoading = false, isVerifyOTP = true)
                return@launch
            }

            if (otpResult is ApiResult.UnknownError) {
                uiEventChannel.send(otpResult)
                state = state.copy(isLoading = false, isVerifyOTP = true, otp = "")
                return@launch
            }

            val signUpResult = authRepository.signUp(
                state.username, state.password, state.email
            )
            uiEventChannel.send(signUpResult)
            state = state.copy(isLoading = false)
        }
    }

    private fun sendOTP() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            when (val result = authRepository.sendOTP(state.email)) {
                is ApiResult.Error -> {
                    uiEventChannel.send(result)
                    state = state.copy(isLoading = false)
                    return@launch
                }
                is ApiResult.UnknownError -> {
                    uiEventChannel.send(result)
                    state = state.copy(isLoading = false)
                    return@launch
                }
                is ApiResult.Success -> {
                    state = state.copy(isLoading = false, isVerifyOTP = true)
                }
                else -> {
                    // Do nothing
                }
            }
        }
    }

}