package com.mdev.chatapp.data.local.app_entry

import com.mdev.chatapp.domain.user_entry.LocalUserManager
import com.mdev.chatapp.util.Constants
import com.mdev.chatapp.util.DataStoreHelper
import kotlinx.coroutines.flow.Flow

class LocalUserManagerImpl(
    private val dataStore: DataStoreHelper
) : LocalUserManager {

    // Save the app entry status to the data store.
    override suspend fun saveAppEntry() {
        dataStore.setBool(Constants.APP_ENTRY, true)
    }

    override fun readAppEntry(): Flow<Boolean> {
        return dataStore.readAppEntry()
    }

}
