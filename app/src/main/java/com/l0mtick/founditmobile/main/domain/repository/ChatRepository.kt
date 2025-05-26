package com.l0mtick.founditmobile.main.domain.repository

import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.domain.model.Chat
import com.l0mtick.founditmobile.main.domain.model.ChatData

interface ChatRepository {

    suspend fun getAllCurrentUserChats(): Result<List<Chat>?, DataError.Network>
    suspend fun getChatData(chatId: Int): Result<ChatData, DataError.Network>
    suspend fun sendMessage(chatId: Int, content: String): Result<Unit, DataError.Network>

}