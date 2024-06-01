package com.mdev.chatapp.ui.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.data.remote.chat.model.HistoryResponse
import com.mdev.chatapp.data.remote.chat.model.Message
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
    savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    private val dataStore: DataStoreHelper,
    private val stringProvider: StringProvider
) : ViewModel() {

    private val conversationId = savedStateHandle.get<String>("conversationId") ?: INIT_CONVERSATION_ID
    var state by mutableStateOf(ChatState(conversationId = conversationId))


    var historyChats by mutableStateOf(HistoryResponse(listOf()))
    var followUpQuestion by mutableStateOf(listOf<Message>())

    init {
        loadCurrentUser()
        if (conversationId != INIT_CONVERSATION_ID)
            refreshHistory()
    }

    fun onEvent(event: ChatUIEvent) {
        when (event) {
            is ChatUIEvent.OnInputMessageChanged -> {
                state = state.copy(inputMessage = event.value)
            }

            is ChatUIEvent.SendMessage -> {
                sendMessage(event.message)
            }
            is ChatUIEvent.IsListening -> {
                state = state.copy(isListening = event.isListening)
            }
            is ChatUIEvent.OnInputMessageChangedByListening -> {
                state = state.copy(inputMessage = state.inputMessage + " " + event.value)            }
            is ChatUIEvent.CancelSendMessage -> {
                // TODO
            }
            is ChatUIEvent.OnViewFollowUpQuestion -> {
                state = state.copy(isViewFollowUpQuestion = !state.isViewFollowUpQuestion)
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

    private fun sendMessage(message: String = state.inputMessage) {

        resetInput()
        historyChats = historyChats.copy(
            messages = historyChats.messages
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
                }
                else {
                    historyChats = historyChats.copy(
                        messages = historyChats.messages
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
                    historyChats = historyChats.copy(
                        messages = historyChats.messages
                                + result.response!!.messages[0]
                    )
                    followUpQuestion = listOf(
                        result.response.messages[1],
                        result.response.messages[2],
                        result.response.messages[3]
                    )
                }

                is ApiResult.Error -> {
                    val errorMessage = stringProvider.getString(result.message)
                    historyChats = historyChats.copy(
                        messages = historyChats.messages
                                + Message(
                            role = "assistant",
                            content_type = "text",
                            type = "",
                            content = "Api error has occurred: $errorMessage"
                        )
                    )
                }

                is ApiResult.UnknownError -> {
                    historyChats = historyChats.copy(
                        messages = historyChats.messages
                                + Message(
                            role = "assistant",
                            content_type = "text",
                            type = "",
                            content = "Unknown error has occurred: ${result.message}"
                        )
                    )
                }
                else -> {
                    // Do nothing
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
                when (val result = chatRepository.loadHistoryChat(state.conversationId)) {
                    is ApiResult.Success -> {
                        historyChats = result.response!!
                    }
                    is ApiResult.Error -> {
                        state = state.copy(isErrorOccurred = true)
                        val errorMessage = stringProvider.getString(result.message)
                        historyChats = HistoryResponse(
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
                        historyChats = HistoryResponse(
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
                    else -> {
                        // Do nothing
                    }
                }
            }
            state = state.copy(isLoading = false)
        }
    }

}