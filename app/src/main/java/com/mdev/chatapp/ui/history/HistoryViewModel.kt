package com.mdev.chatapp.ui.history

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.data.local.conversation.ConversationModel
import com.mdev.chatapp.data.remote.history.Model.Conversation
import com.mdev.chatapp.domain.repository.remote.ChatRepository
import com.mdev.chatapp.domain.repository.remote.HistoryRepository
import com.mdev.chatapp.domain.result.ApiResult
import com.mdev.chatapp.util.Constants
import com.mdev.chatapp.util.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val dataStore: DataStoreHelper
): ViewModel() {
    var state by mutableStateOf(HistoryState())


    private var currentUserId = ""

    init {
        viewModelScope.launch {
            currentUserId = dataStore.getString(Constants.CURRENT_USER_ID)!!
            getConversations()
        }
    }
    private fun getConversations() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            when (val result = historyRepository.getConversations(currentUserId)) {
                is ApiResult.Success -> {
                    state = state.copy(conversations = result.response!!.data)
                }
                else -> {
                    Log.d("HistoryViewModel", "getConversations: ${result.data.toString()}")
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    fun onEvent(event: HistoryUiEvent) {
        when (event) {
            is HistoryUiEvent.OnEditSaveClick -> onEditClick(event.conversation)
            is HistoryUiEvent.OnTitleChanged -> onTitleChanged(event.newTitle)
        }
    }

    private fun onEditClick(conversation: Conversation) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            when (val result = historyRepository.editConversationTitle(conversation.conversation_id, state.newTitle)) {
                is ApiResult.Success -> {
                    getConversations()
                }
                else -> {
                    Log.d("HistoryViewModel", "onEditClick: ${result.data.toString()}")
                }
            }
        }
    }

    private fun onTitleChanged(newTitle: String) {
        viewModelScope.launch {
            state = state.copy(newTitle = newTitle)
        }
    }


}