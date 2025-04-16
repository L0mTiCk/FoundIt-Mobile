package com.l0mtick.founditmobile.common.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int,
    val username: String,
    val email: String,
    val level: Int,
    val itemCount: Int,
    val createdAt: Long,
    val logoUrl: String?
)