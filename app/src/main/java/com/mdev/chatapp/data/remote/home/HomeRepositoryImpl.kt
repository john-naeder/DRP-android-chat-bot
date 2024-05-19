package com.mdev.chatapp.data.remote.home

import com.mdev.chatapp.domain.repository.HomeRepository
import com.mdev.chatapp.ui.auth.event_state.AuthResult
import com.mdev.chatapp.ui.home.HomeResult
import com.mdev.chatapp.util.DataStoreHelper

class HomeRepositoryImpl (
    private val dataStore: DataStoreHelper
) : HomeRepository {

    private val JWT = "jwt_"
    private val CURRENT_USER = "current_user"
    override suspend fun logout(): HomeResult {
        dataStore.remove(JWT + CURRENT_USER)
        dataStore.remove(CURRENT_USER)

        return HomeResult.Logout
    }
}