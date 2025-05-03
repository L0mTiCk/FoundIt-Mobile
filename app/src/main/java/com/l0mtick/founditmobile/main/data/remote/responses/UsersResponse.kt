package com.l0mtick.founditmobile.main.data.remote.responses

import com.l0mtick.founditmobile.main.data.remote.dto.UserDTO
import kotlinx.serialization.Serializable

@Serializable
data class UsersResponse(
    val users: List<UserDTO>?,
    val updateTime: Long
)
