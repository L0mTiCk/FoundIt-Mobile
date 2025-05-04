package com.l0mtick.founditmobile.main.data.repository

import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.data.util.toModel
import com.l0mtick.founditmobile.main.domain.model.Chat
import com.l0mtick.founditmobile.main.domain.repository.ChatRepository
import com.l0mtick.founditmobile.main.domain.repository.MainApi

class ChatRepositoryImpl(private val mainApi: MainApi) : ChatRepository {
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
}