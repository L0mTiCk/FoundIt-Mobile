package com.l0mtick.founditmobile.main.data.remote.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateChatRequest(
    val interlocutorId: Int,
    val lostItemId: Int
)
