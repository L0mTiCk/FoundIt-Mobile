package com.l0mtick.founditmobile.main.data.repository

import com.l0mtick.founditmobile.common.domain.repository.LocalStorage
import com.l0mtick.founditmobile.main.domain.repository.AddItemRepository

class AddItemRepositoryImpl(
    private val localStorage: LocalStorage
): AddItemRepository {
    override suspend fun getUserLevel(): Int? {
        return localStorage.getLevel()
    }
}