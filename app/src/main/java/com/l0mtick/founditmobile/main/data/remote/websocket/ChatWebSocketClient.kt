package com.l0mtick.founditmobile.main.data.remote.websocket

import com.l0mtick.founditmobile.main.data.remote.dto.MessageDTO
import kotlinx.coroutines.flow.Flow

interface ChatWebSocketClient {
    /**
     * Подключение к WebSocket серверу
     */
    suspend fun connect()
    
    /**
     * Отключение от WebSocket сервера
     */
    suspend fun disconnect()
    
    /**
     * Отправка сообщения через WebSocket
     * @param chatId ID чата
     * @param content Содержимое сообщения
     */
    suspend fun sendMessage(chatId: Int, content: String)
    
    /**
     * Поток входящих сообщений
     */
    val incomingMessages: Flow<MessageDTO>
    
    /**
     * Состояние подключения
     */
    val connectionState: Flow<ConnectionState>
    
    /**
     * Состояния подключения к WebSocket
     */
    enum class ConnectionState {
        CONNECTING,
        CONNECTED,
        DISCONNECTED,
        ERROR
    }
}