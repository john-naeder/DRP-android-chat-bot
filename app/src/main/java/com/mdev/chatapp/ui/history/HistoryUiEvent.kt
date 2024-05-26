package com.mdev.chatapp.ui.history

import android.service.quicksettings.Tile
import com.mdev.chatapp.data.local.conversation.ConversationModel

sealed class HistoryUiEvent() {
    data class OnDeleteHistory(val conversationModel: ConversationModel) : HistoryUiEvent()
    data class OnChangeConversationTitle(val conversationModel: ConversationModel, val newTitle: String) : HistoryUiEvent()

}