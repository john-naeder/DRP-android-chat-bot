package com.mdev.chatapp.ui.auth.common

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import com.mdev.chatapp.R
import com.mdev.chatapp.ui.auth.AuthViewModel
import com.mdev.chatapp.ui.auth.event_state.AuthUiEvent
import com.mdev.chatapp.ui.theme.focusedTextFieldText
import com.mdev.chatapp.ui.theme.textFieldContainer
import com.mdev.chatapp.ui.theme.unfocusedTextFieldText


@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    label: Int,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit
) {
    TextField(
        modifier = modifier,
        label = { Text(text = stringResource(label), style = MaterialTheme.typography.labelMedium) },
        value = password,
        onValueChange = onPasswordChange,
        singleLine = true,
        placeholder = { Text("Password") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { onPasswordVisibilityChange(!passwordVisible) }) {
                Icon(imageVector = image, contentDescription = description)
            }
        },
        colors = TextFieldDefaults.colors(
            focusedPlaceholderColor = MaterialTheme.colorScheme.focusedTextFieldText,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
            focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer
        )
    )
}

@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    trailing: String,
    viewModel: AuthViewModel = hiltViewModel(),
){
    val state = viewModel.state
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    when (label) {
        R.string.password, R.string.password_signup, R.string.repassword -> {
            PasswordTextField(
                modifier = modifier,
                label = label,
                password = password,
                onPasswordChange = { newPassword ->
                    password = newPassword
                    viewModel.onEvent(AuthUiEvent.PasswordChanged(newPassword))
                },
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = { newPasswordVisible ->
                    passwordVisible = newPasswordVisible
                }
            )
        }

        R.string.username_login, R.string.username_signup -> {
            TextField(
                modifier = modifier,
                label = {
                    Text(
                        text = stringResource(label),
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                value = state.username,
                onValueChange = { newUsername ->
                    viewModel.onEvent(AuthUiEvent.UsernameChanged(newUsername))
                },
                singleLine = true,
                placeholder = { Text("Username") },
                colors = TextFieldDefaults.colors(
                    focusedPlaceholderColor = MaterialTheme.colorScheme.focusedTextFieldText,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                    focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer
                )
            )
        }
        R.string.email -> {
            TextField(
                modifier = modifier,
                label = {
                    Text(
                        text = stringResource(label),
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                value = state.email,
                onValueChange = { newEmail ->
                    viewModel.onEvent(AuthUiEvent.EmailChanged(newEmail))
                },
                singleLine = true,
                placeholder = { Text("Email") },
                colors = TextFieldDefaults.colors(
                    focusedPlaceholderColor = MaterialTheme.colorScheme.focusedTextFieldText,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                    focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer
                )
            )
        }
    }
}

