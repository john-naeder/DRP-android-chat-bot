package com.mdev.chatapp.ui.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.data.remote.chat.model.HistoryResponse
import com.mdev.chatapp.data.remote.chat.model.Message
import com.mdev.chatapp.domain.repository.remote.ChatRepository
import com.mdev.chatapp.domain.result.ApiResult
import com.mdev.chatapp.util.Constants
import com.mdev.chatapp.util.Constants.INIT_CONVERSATION_ID
import com.mdev.chatapp.util.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val dataStore: DataStoreHelper
) : ViewModel() {
    var state by mutableStateOf(ChatState())
    private val uiEventChannel = Channel<ApiResult<Unit>>()
    var historyChats = mutableStateOf(HistoryResponse(listOf()))
    var followUpQuestion = mutableStateOf(listOf<Message>())

    init {
        loadCurrentUser()
        refreshHistory()
    }

    fun onEvent(event: ChatUIEvent) {
        when (event) {
            is ChatUIEvent.OnInputMessageChanged -> {
                state = state.copy(inputMessage = event.value)
            }

            is ChatUIEvent.SendMessage -> {
                sendMessage()
            }

            ChatUIEvent.CancelSendMessage -> {
                // TODO
            }
        }
    }

    private fun changeAllowToInput(
        isAllowed: Boolean
    ) {
        state = state.copy(isInputVisibility = isAllowed, isWaitingForResponse = !isAllowed)
    }

    private fun resetInput() {
        state = state.copy(inputMessage = "")
    }

    private fun sendMessage() {
        historyChats.value = historyChats.value.copy(
            messages = historyChats.value.messages
                    + Message(
                role = "user",
                content_type = "text",
                type = "",
                content = state.inputMessage
            )
        )
        viewModelScope.launch {
            if (state.conversationId == INIT_CONVERSATION_ID) {
                initConversation()
            }
            changeAllowToInput(false)
            val result = chatRepository.newChat(
                userId = state.currentUserId,
                message = state.inputMessage,
                conversationId = state.conversationId
            )
            when (result) {
                is ApiResult.Success -> {
                    historyChats.value = historyChats.value.copy(
                        messages = historyChats.value.messages
                                + result.response!!.messages[0]
                    )
                    followUpQuestion.value = listOf(
                        result.response.messages[1],
                        result.response.messages[2],
                        result.response.messages[3]
                    )
                    resetInput()
                }

                is ApiResult.Error -> {
                    historyChats.value = historyChats.value.copy(
                        messages = historyChats.value.messages
                                + Message(
                            role = "assistant",
                            content_type = "text",
                            type = "",
                            content = "Api error has occurred: ${result.message}"
                        )
                    )
                }

                is ApiResult.UnknownError -> {
                    historyChats.value = historyChats.value.copy(
                        messages = historyChats.value.messages
                                + Message(
                            role = "assistant",
                            content_type = "text",
                            type = "",
                            content = "Unknown error has occurred: ${result.message}"
                        )
                    )
                }

                else -> {
                    // do nothing
                }

            }
            resetInput()
            changeAllowToInput(true)
        }
    }

    private fun initConversation() {
        viewModelScope.launch {
            val result = chatRepository.initConversation(state.currentUserId)
            if (result is ApiResult.Success) {
                state = state.copy(conversationId = result.response!!.conversations_id)
            }
        }
    }

    private fun refreshHistory() {
        loadHistory()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            state = state.copy(currentUserId = dataStore.getString(Constants.CURRENT_USER_ID)!!)
        }
    }


    private fun loadHistory() {
        viewModelScope.launch {
            if (state.conversationId !== INIT_CONVERSATION_ID) {
                val result = chatRepository.loadHistoryChat(state.conversationId)
                if (result is ApiResult.Success) {
                    historyChats.value = result.response!!
                }
            }
        }
    }


}