package com.mdev.chatapp.ui.history

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.domain.result.ApiResult
import com.mdev.chatapp.util.Constants
import com.mdev.chatapp.util.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
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
                }
                else -> {
                    Log.d("HistoryViewModel", "getConversations: ${result.data.toString()}")
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