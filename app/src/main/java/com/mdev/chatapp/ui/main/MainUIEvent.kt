package com.mdev.chatapp.ui.main

sealed class MainUIEvent {
    data object OnSwitchTheme: MainUIEvent()
    data class OnSwitchLanguage(val language: String): MainUIEvent()
}
