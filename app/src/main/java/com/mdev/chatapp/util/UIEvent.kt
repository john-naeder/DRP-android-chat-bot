package com.mdev.chatapp.util

import com.mdev.chatapp.ui.navgraph.Route

sealed class UIEvent {
    data object Logout : UIEvent()
    data object Back : UIEvent()
    data class NavigateTo(val route: Route) : UIEvent()
}