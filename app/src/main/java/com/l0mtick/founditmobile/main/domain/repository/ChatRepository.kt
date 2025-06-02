package com.l0mtick.founditmobile.main.domain.repository

import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.data.remote.websocket.ChatWebSocketClient
import com.l0mtick.founditmobile.main.domain.model.Chat
import com.l0mtick.founditmobile.main.domain.model.ChatData
import com.l0mtick.founditmobile.main.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun getAllCurrentUserChats(): Result<List<Chat>?, DataError.Network>
    suspend fun getChatData(chatId: Int): Result<ChatData, DataError.Network>
    suspend fun sendMessage(chatId: Int, content: String): Result<Unit, DataError.Network>
    suspend fun deleteChat(chatId: Int): Result<Unit, DataError.Network>
    
    /**
     * Отправка сообщения об удалении чата через WebSocket
     * @param chatId ID чата для удаления
     */
    suspend fun sendDeleteChatMessage(chatId: Int): Result<Unit, DataError.Network>
    
    /**
     * Подключение к WebSocket серверу
     */
    suspend fun connectToWebSocket()
    
    /**
     * Отключение от WebSocket сервера
     */
    suspend fun disconnectFromWebSocket()
    
    /**
     * Получение потока состояния подключения WebSocket
     */
    fun getWebSocketConnectionState(): Flow<ChatWebSocketClient.ConnectionState>
    
    /**
     * Получение потока входящих сообщений
     */
    fun getIncomingMessages(): Flow<Message>
    
    /**
     * Получение потока событий удаления чата
     */
    fun getChatDeletedEvents(): Flow<Int>

}