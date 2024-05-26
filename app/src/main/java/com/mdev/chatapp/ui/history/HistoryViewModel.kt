package com.mdev.chatapp.ui.history

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.data.local.conversation.ConversationModel
import com.mdev.chatapp.domain.repository.local.ConversationRepository
import com.mdev.chatapp.util.Constants
import com.mdev.chatapp.util.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val conversationRepository: ConversationRepository,
    private val dataStore: DataStoreHelper
): ViewModel() {
    val conversations = MutableLiveData<List<ConversationModel>>()

    init {
        viewModelScope.launch {
            val userId = dataStore.getString(Constants.CURRENT_USER_ID) ?: ""
            conversationRepository.getAllConversationByUserId(userId).collect { data ->
                conversations.value = data
            }
        }
    }

    fun onEvent(event: HistoryUiEvent) {
        when (event) {
            is HistoryUiEvent.OnChangeConversationTitle -> onEditClick(event.conversationModel, event.newTitle)
            is HistoryUiEvent.OnDeleteHistory-> onDeleteClick(event.conversationModel)
        }
    }

    private fun onEditClick(conversationModel: ConversationModel, newTitle: String) {
        viewModelScope.launch {
            conversationRepository.insertConversation(conversationModel.copy(conversation_title = newTitle))
        }
    }

    private fun onDeleteClick(conversationModel: ConversationModel) {
        viewModelScope.launch {
            conversationRepository.deleteConversationById(conversationModel.conversation_id)
        }
    }
}