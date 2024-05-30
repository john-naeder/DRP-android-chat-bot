package com.mdev.chatapp.ui.common.nav_drawer

sealed class NavDrawerUIEvent {
    data object Logout : NavDrawerUIEvent()
    data object Home: NavDrawerUIEvent()
    data object NewChat : NavDrawerUIEvent()
    data object History : NavDrawerUIEvent()
    data object About : NavDrawerUIEvent()
    data object Settings : NavDrawerUIEvent()
    data object Profile : NavDrawerUIEvent()
    data object Back: NavDrawerUIEvent()
}