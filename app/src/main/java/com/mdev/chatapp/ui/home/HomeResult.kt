package com.mdev.chatapp.ui.home

sealed class HomeResult {
    data object Logout: HomeResult()
}

