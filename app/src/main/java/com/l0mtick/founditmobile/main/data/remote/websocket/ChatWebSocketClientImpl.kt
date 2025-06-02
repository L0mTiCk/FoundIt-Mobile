package com.l0mtick.founditmobile.main.data.remote.websocket

import android.util.Log
import com.l0mtick.founditmobile.BuildConfig
import com.l0mtick.founditmobile.common.domain.repository.LocalStorage
import com.l0mtick.founditmobile.main.data.remote.dto.MessageDTO
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.header
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json

class ChatWebSocketClientImpl(
    private val httpClient: HttpClient,
    private val json: Json,
    private val localStorage: LocalStorage
) : ChatWebSocketClient {

    private val TAG = "ChatWebSocketClient"
    private val webSocketScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var webSocketSession: DefaultClientWebSocketSession? = null

    private val _connectionState = MutableStateFlow(ChatWebSocketClient.ConnectionState.DISCONNECTED)
    override val connectionState: Flow<ChatWebSocketClient.ConnectionState> = _connectionState.asStateFlow()

    private val _incomingMessages = MutableSharedFlow<MessageDTO>(replay = 0)
    override val incomingMessages: Flow<MessageDTO> = _incomingMessages.asSharedFlow()
    
    private val _chatDeletedEvents = MutableSharedFlow<Int>(replay = 0)
    override val chatDeletedEvents: Flow<Int> = _chatDeletedEvents.asSharedFlow()

    override suspend fun connect() {
        if (_connectionState.value == ChatWebSocketClient.ConnectionState.CONNECTED) {
            Log.d(TAG, "Already connected to WebSocket")
            return
        }

        try {
            _connectionState.value = ChatWebSocketClient.ConnectionState.CONNECTING
            
            val baseUrl = BuildConfig.BASE_URL.removePrefix("http://").removeSuffix("/")
            Log.d(TAG, "Connecting to WebSocket at $baseUrl/api/user/ws/chat")
            val authToken = localStorage.getToken() ?: throw IllegalArgumentException("Local token is empty")
            httpClient.webSocket(
                method = HttpMethod.Get,
                host = baseUrl,
                path = "/api/user/ws/chat",
                request = {
                    header("Authorization", "Bearer $authToken")
                }
            ) {
                webSocketSession = this
                _connectionState.value = ChatWebSocketClient.ConnectionState.CONNECTED
                Log.d(TAG, "Connected to WebSocket")
                
                try {
                    for (frame in incoming) {
                        when (frame) {
                            is Frame.Text -> {
                                val text = frame.readText()
                                Log.d(TAG, "Received message: $text")
                                try {
                                    val message = json.decodeFromString<WebSocketMessage>(text)
                                    when (message) {
                                        is WebSocketMessage.MessageReceived -> {
                                            _incomingMessages.emit(message.message)
                                        }
                                        is WebSocketMessage.ChatDeleted -> {
                                            Log.d(TAG, "Received chat deleted event for chat ID: ${message.chatId}")
                                            _chatDeletedEvents.emit(message.chatId)
                                        }
                                        else -> Log.d(TAG, "Received other WebSocket message type: $message")
                                    }
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error parsing WebSocket message", e)
                                }
                            }
                            else -> Log.d(TAG, "Received other frame: $frame")
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error in WebSocket connection", e)
                    _connectionState.value = ChatWebSocketClient.ConnectionState.ERROR
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to connect to WebSocket", e)
            _connectionState.value = ChatWebSocketClient.ConnectionState.ERROR
        } finally {
            if (_connectionState.value != ChatWebSocketClient.ConnectionState.ERROR) {
                _connectionState.value = ChatWebSocketClient.ConnectionState.DISCONNECTED
            }
        }
    }

    override suspend fun disconnect() {
        try {
            webSocketSession?.close()
            webSocketSession = null
            _connectionState.value = ChatWebSocketClient.ConnectionState.DISCONNECTED
            Log.d(TAG, "Disconnected from WebSocket")
        } catch (e: Exception) {
            Log.e(TAG, "Error disconnecting from WebSocket", e)
        }
    }

    override suspend fun sendMessage(chatId: Int, content: String) {
        if (_connectionState.value != ChatWebSocketClient.ConnectionState.CONNECTED) {
            Log.e(TAG, "Cannot send message: not connected to WebSocket")
            return
        }

        try {
            val message = WebSocketMessage.SendMessage(chatId, content)
            val jsonMessage = json.encodeToString<WebSocketMessage>(message)
            Log.d(TAG, "Sending message: $jsonMessage")
            webSocketSession?.send(Frame.Text(jsonMessage))
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message", e)
        }
    }
    
    override suspend fun sendDeleteChatMessage(chatId: Int) {
        if (_connectionState.value != ChatWebSocketClient.ConnectionState.CONNECTED) {
            Log.e(TAG, "Cannot send delete chat message: not connected to WebSocket")
            return
        }

        try {
            val message = WebSocketMessage.DeleteChat(chatId)
            val jsonMessage = json.encodeToString<WebSocketMessage>(message)
            Log.d(TAG, "Sending delete chat message: $jsonMessage")
            webSocketSession?.send(Frame.Text(jsonMessage))
        } catch (e: Exception) {
            Log.e(TAG, "Error sending delete chat message", e)
        }
    }
}