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
        title = "App chat DRP.",
        description = "Được phát triển bởi 3 chú be vjp zo.",
        image = R.drawable.onboarding1
    ),
    Page(
        title = "Mục đích",
        description = "Cung cấp chat bot hỗ trợ tư vấn ăn uống.",
        image = R.drawable.onboarding2
    ),
    Page(
        title = "Enzuyzepzaivl",
        description = "Hết ý tưởng ghi rồi",
        image = R.drawable.onboarding3
    )
)