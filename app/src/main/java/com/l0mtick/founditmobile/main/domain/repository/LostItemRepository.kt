package com.l0mtick.founditmobile.main.domain.repository

import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.domain.model.Chat
import com.l0mtick.founditmobile.main.domain.model.LostItem
import com.l0mtick.founditmobile.main.domain.model.PaginatedData
import com.l0mtick.founditmobile.main.domain.model.User

interface LostItemRepository {

    suspend fun searchLostItems(
        searchQuery: String?,
        categoryIds: List<Int>?,
        userLatitude: Double,
        userLongitude: Double,
        radiusKm: Double?,
        afterId: Int?,
        limit: Int?,
        date: Long?
    ): Result<PaginatedData<LostItem>, DataError.Network>
    suspend fun getItemsForMap(
        userLatitude: Double,
        userLongitude: Double,
        radiusKm: Double?
    ): Result<List<LostItem>, DataError.Network>

    suspend fun getDetailedLostItem(itemId: Int): Result<Pair<LostItem, User>, DataError.Network>

    suspend fun getFavoriteLostItems(): Result<List<LostItem>, DataError.Network>
    suspend fun getUserCreatedLostItems(): Result<List<LostItem>, DataError.Network>

    suspend fun addItemToFavorites(itemId: Int): Result<Unit, DataError.Network>
    suspend fun removeItemFromFavorites(itemId: Int): Result<Unit, DataError.Network>

    suspend fun deleteUserCreatedItem(itemId: Int): Result<Unit, DataError.Network>

    suspend fun createChatForItem(userId: Int, itemId: Int): Result<Chat, DataError.Network>
}