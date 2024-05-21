package com.mdev.chatapp.domain.user_entry.app_entry

import com.mdev.chatapp.domain.user_entry.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadAppEntry (private val localUserManager: LocalUserManager
){
    operator fun invoke(): Flow<Boolean> = localUserManager.readAppEntry()
}