package com.mdev.chatapp.ui.chat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.data.local.conversation.ConversationModel
import com.mdev.chatapp.data.remote.chat.model.HistoryResponse
import com.mdev.chatapp.data.remote.chat.model.Message
import com.mdev.chatapp.domain.repository.local.ConversationRepository
import com.mdev.chatapp.domain.repository.remote.ChatRepository
import com.mdev.chatapp.domain.result.ApiResult
import com.mdev.chatapp.util.Constants
import com.mdev.chatapp.util.Constants.INIT_CONVERSATION_ID
import com.mdev.chatapp.util.DataStoreHelper
import com.mdev.chatapp.util.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    private val conversationRepository: ConversationRepository,
    private val dataStore: DataStoreHelper,
    private val stringProvider: StringProvider
) : ViewModel() {

    val conversationId = savedStateHandle.get<String>("conversationId") ?: INIT_CONVERSATION_ID

    var state by mutableStateOf(ChatState(conversationId = conversationId))

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
        val message = state.inputMessage
        resetInput()
        historyChats.value = historyChats.value.copy(
            messages = historyChats.value.messages
                    + Message(
                role = "user",
                content_type = "text",
                type = "",
                content = message
            )
        )
        viewModelScope.launch {
            changeAllowToInput(false)
            if (state.conversationId == INIT_CONVERSATION_ID) {
                val result = chatRepository.initConversation(state.currentUserId)
                if (result is ApiResult.Success) {
                    state = state.copy(conversationId = result.response!!.conversation_id)
                    conversationRepository.insertConversation(
                        conversationModel = ConversationModel(
                            userId = state.currentUserId,
                            conversationId = result.response.conversation_id,
                            conversationTitle = Constants.NEW_CHAT
                        )
                    )
                }
                else {
                    historyChats.value = historyChats.value.copy(
                        messages = historyChats.value.messages
                                + Message(
                            role = "assistant",
                            content_type = "text",
                            type = "",
                            content = "Failed to start a new conversation",
                        )
                    )
                }
            }

            val result = chatRepository.newChat(
                userId = state.currentUserId,
                message = message,
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
                }

                is ApiResult.Error -> {
                    val errorMessage = stringProvider.getString(result.message)
                    historyChats.value = historyChats.value.copy(
                        messages = historyChats.value.messages
                                + Message(
                            role = "assistant",
                            content_type = "text",
                            type = "",
                            content = "Api error has occurred: $errorMessage"
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
            }
            changeAllowToInput(true)
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
            if (state.isErrorOccurred) {
                return@launch
            }
            state = state.copy(isLoading = true)
            if (state.conversationId !== INIT_CONVERSATION_ID) {
                Log.d("ChatViewModel", "loadHistory: ${state.conversationId}")
                val result = chatRepository.loadHistoryChat(state.conversationId)
                when (result) {
                    is ApiResult.Success -> {
                        historyChats.value = result.response!!
                    }
                    is ApiResult.Error -> {
                        state = state.copy(isErrorOccurred = true)
                        val errorMessage = stringProvider.getString(result.message)
                        historyChats.value = HistoryResponse(
                            messages = listOf(
                                Message(
                                    role = "assistant",
                                    content_type = "text",
                                    type = "",
                                    content = "Api error has occurred: $errorMessage"
                                )
                            )
                        )
                    }
                    is ApiResult.UnknownError -> {
                        state = state.copy(isErrorOccurred = true)
                        historyChats.value = HistoryResponse(
                            messages = listOf(
                                Message(
                                    role = "assistant",
                                    content_type = "text",
                                    type = "",
                                    content = "Unknown error has occurred: ${result.message}"
                                )
                            )
                        )
                    }
                }
            }
            state = state.copy(isLoading = false)
        }
    }
    fun updateConversationId(conversationId: String) {
        Log.d("mine", "updateConversationId: $conversationId")
        state = state.copy(conversationId = conversationId)
        refreshHistory()
    }


}