package com.l0mtick.founditmobile.main.data.repository

import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.data.remote.websocket.ChatWebSocketClient
import com.l0mtick.founditmobile.main.data.util.toModel
import com.l0mtick.founditmobile.main.domain.model.Chat
import com.l0mtick.founditmobile.main.domain.model.ChatData
import com.l0mtick.founditmobile.main.domain.model.Message
import com.l0mtick.founditmobile.main.domain.repository.ChatRepository
import com.l0mtick.founditmobile.main.domain.repository.MainApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepositoryImpl(
    private val mainApi: MainApi,
    private val chatWebSocketClient: ChatWebSocketClient
) : ChatRepository {
    override suspend fun getAllCurrentUserChats(): Result<List<Chat>?, DataError.Network> {
        val result = mainApi.getCurrentUserChats()
        return when(result) {
            is Result.Success -> {
                Result.Success(
                    data = result.data.chats?.map { it.toModel() }
                )
            }
            is Result.Error -> Result.Error(result.error)
        }
    }
    
    override suspend fun getChatData(chatId: Int): Result<ChatData, DataError.Network> {
        val result = mainApi.getChatMessages(chatId)
        return when(result) {
            is Result.Success -> {
                Result.Success(
                    data = result.data.toModel()
                )
            }
            is Result.Error -> Result.Error(result.error)
        }
    }
    
    override suspend fun sendMessage(chatId: Int, content: String): Result<Unit, DataError.Network> {
        chatWebSocketClient.sendMessage(chatId, content)
        return Result.Success(Unit)
    }

    override suspend fun deleteChat(chatId: Int): Result<Unit, DataError.Network> {
        return mainApi.deleteChat(chatId)
    }
    
    override suspend fun connectToWebSocket() {
        chatWebSocketClient.connect()
    }
    
    override suspend fun disconnectFromWebSocket() {
        chatWebSocketClient.disconnect()
    }
    
    override fun getWebSocketConnectionState(): Flow<ChatWebSocketClient.ConnectionState> {
        return chatWebSocketClient.connectionState
    }
    
    override fun getIncomingMessages(): Flow<Message> {
        return chatWebSocketClient.incomingMessages.map { it.toModel() }
    }
}