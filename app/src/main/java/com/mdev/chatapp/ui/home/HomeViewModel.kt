package com.mdev.chatapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.ui.navgraph.Route
import com.mdev.chatapp.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel(){

    private val uiEventChannel = Channel<UIEvent>()
    fun onEvent(event: HomeUiEvent) {
        when(event) {
            is HomeUiEvent.Logout -> {
                sendUIEvent(UIEvent.Logout)
            }
            is HomeUiEvent.NewChat -> {
                sendUIEvent(UIEvent.NavigateTo(Route.ChatScreen))
            }
            is HomeUiEvent.History -> {
                sendUIEvent(UIEvent.NavigateTo(Route.HistoryScreen))
            }
            is HomeUiEvent.About -> {
                sendUIEvent(UIEvent.NavigateTo(Route.AboutScreen))
            }
            is HomeUiEvent.Settings -> {
                sendUIEvent(UIEvent.NavigateTo(Route.SettingsScreen))
            }
        }
    }

    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            uiEventChannel.send(event)
        }
    }

}