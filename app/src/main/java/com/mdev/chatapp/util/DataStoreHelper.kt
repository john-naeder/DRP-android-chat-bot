package com.mdev.chatapp.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import com.mdev.chatapp.ui.main.dataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Suppress("unused")
class DataStoreHelper @Inject constructor (
    context: Context,
) {
    private val datastore: DataStore<Preferences> = context.dataStore
    suspend fun setStringSet(
        key: String,
        value: Set<String>
    ) {
        datastore.edit { it[stringSetPreferencesKey(key)] = value }
    }

    suspend fun getStringSet(
        key: String
    ) = datastore.data.map { it[stringSetPreferencesKey(key)] }.firstOrNull()

    suspend fun getString(
        key: String,
        default: String
    ) = getString(key) ?: default

    suspend fun getString(
        key: String
    ) = datastore.data.map { it[stringPreferencesKey(key)] }.firstOrNull()

    suspend fun setString(
        key: String,
        value: String
    ) {
        datastore.edit { it[stringPreferencesKey(key)] = value }
    }

    suspend fun getInt(
        key: String
    ) = datastore.data.map { it[intPreferencesKey(key)] }.firstOrNull()

    suspend fun setInt(
        key: String,
        value: Int
    ) {
        datastore.edit { it[intPreferencesKey(key)] = value }
    }

    suspend fun getBool(
        key: String
    ) = datastore.data.map { it[booleanPreferencesKey(key)] }.firstOrNull()

    suspend fun setBool(
        key: String,
        value: Boolean
    ) {
        datastore.edit { it[booleanPreferencesKey(key)] = value }
    }

    suspend fun getLong(
        key: String
    ) = datastore.data.map { it[longPreferencesKey(key)] }.firstOrNull()

    suspend fun setLong(
        key: String,
        value: Long
    ) {
        datastore.edit { it[longPreferencesKey(key)] = value }
    }
    suspend fun remove(key: String) {
        datastore.edit { it.remove(stringPreferencesKey(key)) }
    }

    fun readFromDataStore(s: String): Flow<String> {
        return datastore.data.map { it[stringPreferencesKey(s)] ?: "" }
    }

    fun readAppEntry(): Flow<Boolean> {
        return datastore.data.map { it[booleanPreferencesKey(Constants.APP_ENTRY)] ?: false }
    }
}