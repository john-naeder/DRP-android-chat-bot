package com.mdev.chatapp.ui.onboarding

import androidx.annotation.DrawableRes
import com.mdev.chatapp.R

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val image: Int,
)

val pages = listOf(
    Page(
        title = "To Do List Mobile App 2024.",
        description = "Sản phẩm chống cháy bài giữa kỳ của tụi em",
        image = R.drawable.onboarding1
    ),
    Page(
        title = "Bài còn nhiều sai xót về mặt UI/UX",
        description = "Nhóm có không có bông hồng làm đạo diễn nghệ thuật.",
        image = R.drawable.onboarding2
    ),
    Page(
        title = "Tận dụng vào để học MVVM",
        description = "Các khái niệm cơ bản để phát triển ứng dụng Android",
        image = R.drawable.onboarding3
    )
)