package com.mdev.chatapp.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.domain.repository.HomeRepository
import com.mdev.chatapp.ui.auth.AuthState
import com.mdev.chatapp.util.Constants
import com.mdev.chatapp.util.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    dataStoreHelper: DataStoreHelper
): ViewModel(){

    private var state by mutableStateOf(AuthState())
    private val resultChannel = Channel<HomeResult>()
    val result = resultChannel.receiveAsFlow()
    val currentUser: Flow<String> = dataStoreHelper.readFromDataStore(Constants.CURRENT_USER)

    fun onEvent(event: HomeUiEvent) {
        when(event) {
            is HomeUiEvent.Logout -> {
                logout()
            }
            else -> {
                // TODO
            }
        }
    }
    private fun logout() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = homeRepository.logout()
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }

    private fun onNewChat(){
        // TODO
    }

}