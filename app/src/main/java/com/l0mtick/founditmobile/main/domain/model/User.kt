package com.l0mtick.founditmobile.main.domain.model

data class User(
    val id: Int,
    val username: String = "Username",
    val email: String = "email@mail.com",
    val profilePictureUrl: String? = null,
    val level: Int = 1,
    val levelItemsCount: Int = 2,
    val createdAt: Long = 0L
)
