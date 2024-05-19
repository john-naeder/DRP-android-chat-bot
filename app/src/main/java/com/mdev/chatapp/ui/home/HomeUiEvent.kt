package com.mdev.chatapp.ui.home

sealed class HomeUiEvent{
    data object Logout: HomeUiEvent()
}