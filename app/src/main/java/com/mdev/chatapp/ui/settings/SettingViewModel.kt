package com.mdev.chatapp.ui.settings

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.util.Constants
import com.mdev.chatapp.util.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStore: DataStoreHelper
): ViewModel(){
    var state by mutableStateOf(SettingState())

    init{
        getUserLocalSetting()
    }

    fun onEvent(event: SettingUIEvent){
        when(event){
            is SettingUIEvent.OnLanguageChanged -> {
                viewModelScope.launch {
                    updateLanguage(event)
                }
            }
            is SettingUIEvent.OnThemeChanged -> {
                viewModelScope.launch {
                    updateTheme()
                }
            }
        }
    }

    private fun getUserLocalSetting(){
        viewModelScope.launch {
            val theme = dataStore.getString(Constants.THEME, Constants.DEFAULT_THEME)
            val language = dataStore.getString(Constants.LANGUAGE, Constants.DEFAULT_LANGUAGE)
            state = state.copy(isDarkTheme = theme.toBoolean(), language = language)
        }
    }
    private suspend fun updateLanguage(event: SettingUIEvent.OnLanguageChanged){
            dataStore.setString(Constants.LANGUAGE, event.language)
            state = state.copy(language = event.language)
    }
    private suspend fun updateTheme(){
        val theme = !state.isDarkTheme
        dataStore.setString(Constants.THEME, theme.toString())
        state = state.copy(isDarkTheme = theme)
    }
}