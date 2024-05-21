package com.mdev.chatapp.ui.home

import androidx.compose.ui.graphics.vector.ImageVector

data class HomeNavigateItem (
    val title: String = "",
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)