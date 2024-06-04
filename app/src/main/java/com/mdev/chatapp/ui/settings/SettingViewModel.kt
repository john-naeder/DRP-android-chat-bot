package com.mdev.chatapp.ui.settings

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.util.Constants
import com.mdev.chatapp.util.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStoreHelper: DataStoreHelper,
    @ApplicationContext private val appContext: Context
): ViewModel(){
    var state by mutableStateOf(SettingState())

    init{
        getUserLocalSetting()
    }

    fun onEvent(event: SettingUIEvent){
        when(event){
            is SettingUIEvent.OnLanguageChanged -> {
                viewModelScope.launch {
                    updateLanguage(event, appContext)
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
            val theme = dataStoreHelper.getBool(Constants.THEME) ?: false
            val language = dataStoreHelper.getString(Constants.LANGUAGE, Constants.DEFAULT_LANGUAGE)
            state = state.copy(isDarkTheme = theme, language = language)
        }
    }
    private suspend fun updateLanguage(event: SettingUIEvent.OnLanguageChanged, context: Context){
        dataStoreHelper.setString(Constants.LANGUAGE, event.language)
        state = state.copy(language = event.language)
        changeLocales(context, event.language)
    }
    private suspend fun updateTheme(){
        val theme = !state.isDarkTheme
        dataStoreHelper.setBool(Constants.THEME, theme)
        state = state.copy(isDarkTheme = theme)
    }

    private fun changeLocales(context: Context, language: String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            context.getSystemService(LocaleManager::class.java)
                .applicationLocales = LocaleList.forLanguageTags(language)
        } else
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
    }
}