package com.mdev.chatapp.util

import android.content.Context
import javax.inject.Inject

class StringProvider @Inject constructor(private val context: Context) {
    fun getString(resId: Int): String {
        return context.getString(resId)
    }
}