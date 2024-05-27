package com.mdev.chatapp.data.local.conversation

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface ConversationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversationModel: ConversationModel)

    @Query("SELECT * FROM conversationModel WHERE userId = :userId")
    fun getAllConversationByUserId(userId: String): Flow<List<ConversationModel>>

    @Query("DELETE FROM ConversationModel WHERE conversationId = :conversationId")
    suspend fun deleteConversationById(conversationId: String)


}