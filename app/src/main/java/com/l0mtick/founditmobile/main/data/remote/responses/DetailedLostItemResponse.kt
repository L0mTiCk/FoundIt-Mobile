package com.l0mtick.founditmobile.main.data.remote.responses

import com.l0mtick.founditmobile.common.data.remote.dto.UserDTO
import com.l0mtick.founditmobile.main.data.remote.dto.LostItemDTO
import kotlinx.serialization.Serializable

@Serializable
data class DetailedLostItemResponse(
    val item: LostItemDTO,
    val owner: UserDTO
)
