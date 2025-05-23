package com.l0mtick.founditmobile.main.domain.repository

interface AddItemRepository {
    suspend fun getUserLevel(): Int?
}