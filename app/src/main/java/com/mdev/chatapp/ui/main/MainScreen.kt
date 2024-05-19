package com.mdev.chatapp.ui.main

import androidx.compose.runtime.Composable
import com.mdev.chatapp.ui.auth.AuthViewModel

@Composable
fun MainScreen(
    authViewModel: AuthViewModel
) {
    authViewModel.authenticate()
}