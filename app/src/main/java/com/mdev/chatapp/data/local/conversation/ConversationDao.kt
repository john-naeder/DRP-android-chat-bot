package com.mdev.chatapp.data.local.conversation

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface ConversationDao {

    @Insert
    suspend fun insertConversation(conversationModel: ConversationModel)

    @Query("SELECT * FROM conversationModel WHERE user_id = :userId")
    fun getAllConversationByUserId(userId: String): Flow<List<ConversationModel>>

    @Query("DELETE FROM ConversationModel WHERE conversation_id = :conversationId")
    suspend fun deleteConversationById(conversationId: String)

}