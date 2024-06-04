package com.mdev.chatapp.ui.settings

sealed class SettingUIEvent {
    data class OnLanguageChanged(val language: String): SettingUIEvent()
    data object OnThemeChanged: SettingUIEvent()
}