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
        description = "DRP chat bot by John hooin.",
        image = R.drawable.onboarding1
    ),
    Page(
        title = "Aim",
        description = "An approach to providing a personalized list of",
        image = R.drawable.onboarding2
    ),
    Page(
        title = "Enzuyzepzaivl",
        description = "suitable dishes for each individual through a friendly",
        image = R.drawable.onboarding3
    )
)