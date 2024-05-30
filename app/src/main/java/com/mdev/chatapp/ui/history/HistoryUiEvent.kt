package com.mdev.chatapp.ui.history

import android.service.quicksettings.Tile
import com.mdev.chatapp.data.local.conversation.ConversationModel

sealed class HistoryUiEvent {
    data class OnDeleteHistory(val conversationModel: ConversationModel) : HistoryUiEvent()
    data class OnEditSaveClick(val conversationModel: ConversationModel) : HistoryUiEvent()
    data class OnTitleChanged(val newTitle: String) : HistoryUiEvent()
}