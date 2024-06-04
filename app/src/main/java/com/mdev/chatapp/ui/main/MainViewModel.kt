package com.mdev.chatapp.ui.main

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.domain.user_entry.app_entry.AppEntryUserCase
import com.mdev.chatapp.ui.ThemeSetting
import com.mdev.chatapp.ui.navgraph.Route
import com.mdev.chatapp.util.Constants
import com.mdev.chatapp.util.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.USER_SETTINGS)

@HiltViewModel
class MainViewModel @Inject constructor(
    appEntryUserCase: AppEntryUserCase,
    private val dataStoreHelper: DataStoreHelper
) : ViewModel() {
    var state by mutableStateOf(MainState())

    init {
        viewModelScope.launch() {
//            getCurrentTheme()
            appEntryUserCase.readAppEntry().onEach {
                state = if (it) {
                    state.copy(startDestination = Route.AuthNavigator.route)
                } else {
                    state.copy(startDestination = Route.AppStart.route)
                }
                delay(700)
                state = state.copy(isSplashScreen = false)
            }
        }
    }

    fun onUIEvent(event: MainUIEvent) {
        when (event) {
            is MainUIEvent.OnSwitchTheme -> {
                onChangeTheme()
            }

            is MainUIEvent.OnSwitchLanguage -> {
                state = state.copy(startDestination = Route.AuthNavigator.route)
            }
        }
    }

    private fun onChangeTheme() {
        state = state.copy(isDarkTheme = !state.isDarkTheme)
    }

    private fun onChangeLanguage(route: String) {
        state = state.copy(language = route)
    }

    private suspend fun getCurrentTheme() {
        state = state.copy(
            isDarkTheme = dataStoreHelper.getString(Constants.THEME, Constants.DEFAULT_THEME).toBoolean()
        )
    }
}

