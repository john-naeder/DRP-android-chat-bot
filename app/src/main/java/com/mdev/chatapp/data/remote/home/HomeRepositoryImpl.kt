package com.mdev.chatapp.data.remote.home

import com.mdev.chatapp.domain.repository.HomeRepository
import com.mdev.chatapp.ui.home.HomeResult
import com.mdev.chatapp.util.Constants.CURRENT_USER
import com.mdev.chatapp.util.Constants.JWT
import com.mdev.chatapp.util.DataStoreHelper

class HomeRepositoryImpl (
    private val dataStore: DataStoreHelper
) : HomeRepository {
    override suspend fun logout(): HomeResult {
        dataStore.remove(JWT + CURRENT_USER)
        dataStore.remove(CURRENT_USER)

        return HomeResult.Logout
    }
}