package com.mdev.chatapp.domain.user_entry.app_entry

import com.mdev.chatapp.domain.user_entry.LocalUserManager

class SaveAppEntry (
    private val localUserManager: LocalUserManager
){
    suspend operator fun invoke() = localUserManager.saveAppEntry()
}