package com.mdev.chatapp.ui.main

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.domain.user_entry.app_entry.AppEntryUserCase
import com.mdev.chatapp.ui.navgraph.Route
import com.mdev.chatapp.util.Constants
import com.mdev.chatapp.util.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.USER_SETTINGS)

@HiltViewModel
class MainViewModel @Inject constructor(
    appEntryUserCase: AppEntryUserCase,
    private val dataStoreHelper: DataStoreHelper
) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Main) {
            getCurrentTheme()
            appEntryUserCase.readAppEntry().onEach {
                _state.value = if (it) {
                    _state.value.copy(startDestination = Route.AuthNavigator.route)
                } else {
                    _state.value.copy(startDestination = Route.AppStart.route)
                }
                delay(700)
                _state.value = _state.value.copy(isSplashScreen = false)
            }.launchIn(viewModelScope)
        }
    }

    fun onUIEvent(event: MainUIEvent) {
        when (event) {
            is MainUIEvent.OnSwitchTheme -> {
                onChangeTheme()
            }
            is MainUIEvent.OnSwitchLanguage -> {
                _state.value = _state.value.copy(startDestination = Route.AuthNavigator.route)
            }
        }
    }

    private fun onChangeTheme() {
        viewModelScope.launch(Dispatchers.Main) {
            dataStoreHelper.setBool(Constants.THEME, !_state.value.isDarkTheme)
            _state.value = _state.value.copy(isDarkTheme = !_state.value.isDarkTheme)
        }
    }

    private suspend fun getCurrentTheme() {
        _state.value = _state.value.copy(
            isDarkTheme = dataStoreHelper.getBool(Constants.THEME)?: false
        )
    }
}
