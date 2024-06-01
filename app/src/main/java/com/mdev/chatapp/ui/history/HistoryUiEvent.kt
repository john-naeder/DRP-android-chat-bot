package com.mdev.chatapp.ui.history

import android.service.quicksettings.Tile
import com.mdev.chatapp.data.local.conversation.ConversationModel
import com.mdev.chatapp.data.remote.history.Model.Conversation

sealed class HistoryUiEvent {
    data class OnEditSaveClick(val conversation: Conversation) : HistoryUiEvent()
    data class OnTitleChanged(val newTitle: String) : HistoryUiEvent()
}