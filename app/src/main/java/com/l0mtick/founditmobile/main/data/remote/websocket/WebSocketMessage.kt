package com.l0mtick.founditmobile.main.data.remote.websocket

import com.l0mtick.founditmobile.main.data.remote.dto.ChatDTO
import com.l0mtick.founditmobile.main.data.remote.dto.MessageDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class WebSocketMessage {

    @Serializable
    @SerialName("send_message")
    data class SendMessage(val chatId: Int, val content: String) : WebSocketMessage()

    @Serializable
    @SerialName("message_received")
    data class MessageReceived(val message: MessageDTO) : WebSocketMessage()

    @Serializable
    @SerialName("chat_update")
    data class ChatUpdate(val chat: ChatDTO) : WebSocketMessage()
    
    @Serializable
    @SerialName("delete_chat")
    data class DeleteChat(val chatId: Int) : WebSocketMessage()
    
    @Serializable
    @SerialName("chat_deleted")
    data class ChatDeleted(val chatId: Int) : WebSocketMessage()
}