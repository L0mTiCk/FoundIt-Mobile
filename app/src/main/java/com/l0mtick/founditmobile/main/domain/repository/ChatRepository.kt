package com.l0mtick.founditmobile.main.domain.repository

import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.domain.model.Chat

interface ChatRepository {

    suspend fun getAllCurrentUserChats(): Result<List<Chat>?, DataError.Network>

}