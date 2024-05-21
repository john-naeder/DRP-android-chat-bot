package com.mdev.chatapp.ui.home

sealed class HomeUiEvent{
    data object Logout: HomeUiEvent()
    data object NewChat: HomeUiEvent()
    data object History: HomeUiEvent()
    data object About: HomeUiEvent()
    data object Settings: HomeUiEvent()
}