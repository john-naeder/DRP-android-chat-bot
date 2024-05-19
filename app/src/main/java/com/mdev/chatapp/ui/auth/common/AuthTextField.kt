package com.mdev.chatapp.ui.auth.common

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.mdev.chatapp.R
import com.mdev.chatapp.ui.auth.event_state.AuthUiEvent
import com.mdev.chatapp.ui.auth.AuthViewModel
import com.mdev.chatapp.ui.theme.focusedTextFieldText
import com.mdev.chatapp.ui.theme.textFieldContainer
import com.mdev.chatapp.ui.theme.unfocusedTextFieldText


@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    trailing: String,
    viewModel: AuthViewModel = hiltViewModel(),
){
    val state = viewModel.state
    TextField(
        modifier = modifier,
        label = { Text(text = stringResource(label), style = MaterialTheme.typography.labelMedium) },
        value = when(label){
            R.string.username_login -> state.username
            R.string.password -> state.password
            R.string.username_signup -> state.username
            R.string.password_signup -> state.password
            R.string.repassword -> state.rePassword
            else -> state.email
        },
        colors = TextFieldDefaults.colors(
            focusedPlaceholderColor = MaterialTheme.colorScheme.focusedTextFieldText,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
            focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer
        ),
        onValueChange = {
            when(label){
                R.string.username_signup-> viewModel.onEvent(AuthUiEvent.UsernameChanged(it))
                R.string.password_signup -> viewModel.onEvent(AuthUiEvent.PasswordChanged(it))
                R.string.repassword -> viewModel.onEvent(AuthUiEvent.RePasswordChanged(it))
                R.string.username_login -> viewModel.onEvent(AuthUiEvent.UsernameChanged(it))
                R.string.password -> viewModel.onEvent(AuthUiEvent.PasswordChanged(it))
                else -> viewModel.onEvent(AuthUiEvent.EmailChanged(it))
            }
        },
        trailingIcon = {
            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    text = trailing,
                    style = MaterialTheme.
                            typography.
                            labelMedium.copy(fontWeight = FontWeight.Medium))
            }
        }
    )
}