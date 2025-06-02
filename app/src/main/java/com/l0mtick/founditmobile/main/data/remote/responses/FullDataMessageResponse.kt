package com.l0mtick.founditmobile.main.data.remote.responses

import com.l0mtick.founditmobile.common.data.remote.dto.UserDTO
import com.l0mtick.founditmobile.main.data.remote.dto.MessageDTO
import kotlinx.serialization.Serializable

@Serializable
data class FullDataMessageResponse(
    val interlocutor: UserDTO,
    val itemTitle: String,
    val itemDescription: String,
    val itemPictureUrl: String,
    val messages: List<MessageDTO>
)
