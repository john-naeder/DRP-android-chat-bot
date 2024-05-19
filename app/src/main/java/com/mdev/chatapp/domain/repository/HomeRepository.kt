package com.mdev.chatapp.domain.repository

import com.mdev.chatapp.ui.home.HomeResult

interface HomeRepository {
    suspend fun logout(): HomeResult
}