package com.mdev.chatapp.ui.history

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.data.local.conversation.ConversationModel
import com.mdev.chatapp.data.remote.chat.model.GetConversationIdsResponse
import com.mdev.chatapp.domain.repository.local.ConversationRepository
import com.mdev.chatapp.domain.result.ApiResult
import com.mdev.chatapp.ui.auth.AuthState
import com.mdev.chatapp.util.Constants
import com.mdev.chatapp.util.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
//    private val conversationRepository: ConversationRepository,
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
                    Log.d("HistoryViewModel", "Success: ${result.response.data}")
                }
                is ApiResult.Error -> {
                    Log.e("HistoryViewModel", "Error: ${result.message}")
                }
                is ApiResult.UnknownError -> {
                    Log.e("HistoryViewModel", "Unknown error: ${result.message}")
                }
                else -> {
                    // Do nothing
                }
            }
            state = state.copy(isLoading = false)
        }
    }

//    fun onEvent(event: HistoryUiEvent) {
//        when (event) {
//            is HistoryUiEvent.OnEditSaveClick -> onEditClick(event.conversationModel)
//            is HistoryUiEvent.OnDeleteHistory -> onDeleteClick(event.conversationModel)
//            is HistoryUiEvent.OnTitleChanged -> onTitleChanged(event.newTitle)
//        }
//    }

//    private fun onEditClick(conversationModel: ConversationModel) {
//        viewModelScope.launch {
//            conversationRepository.insertConversation(conversationModel.copy(conversationTitle = state.newTitle))
//        }
//
//    }
//
//    private fun onDeleteClick(conversation: ConversationModel) {
//        viewModelScope.launch {
//            conversationRepository.deleteConversationById(conversation.conversationId)
//        }
//    }

//    private fun onTitleChanged(title: String) {
//        state = state.copy(newTitle = title)
//    }
}