package com.mdev.chatapp.ui.onboarding

sealed class OnBoardingEvent{
    data object SaveAppEntry: OnBoardingEvent()
}