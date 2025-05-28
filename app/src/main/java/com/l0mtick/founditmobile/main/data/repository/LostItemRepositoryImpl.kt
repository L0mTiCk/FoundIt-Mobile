package com.l0mtick.founditmobile.main.data.repository

import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.data.util.toModel
import com.l0mtick.founditmobile.main.domain.model.Chat
import com.l0mtick.founditmobile.main.domain.model.LostItem
import com.l0mtick.founditmobile.main.domain.model.PaginatedData
import com.l0mtick.founditmobile.main.domain.model.User
import com.l0mtick.founditmobile.main.domain.repository.LostItemRepository
import com.l0mtick.founditmobile.main.domain.repository.MainApi

class LostItemRepositoryImpl(private val mainApi: MainApi) : LostItemRepository {

    override suspend fun searchLostItems(
        searchQuery: String?,
        categoryIds: List<Int>?,
        userLatitude: Double,
        userLongitude: Double,
        radiusKm: Double?,
        afterId: Int?,
        limit: Int?,
        date: Long?
    ): Result<PaginatedData<LostItem>, DataError.Network> {
        val resultDTO = mainApi.searchLostItems(
            searchQuery = searchQuery,
            categoryIds = categoryIds,
            userLatitude = userLatitude,
            userLongitude = userLongitude,
            radiusKm = radiusKm,
            afterId = afterId,
            limit = limit,
            date = date
        )

        return when (resultDTO) {
            is Result.Success -> {
                val domainData = resultDTO.data.toModel({ it.toModel() })
                Result.Success(domainData)
            }

            is Result.Error -> {
                Result.Error(resultDTO.error)
            }
        }
    }

    override suspend fun getItemsForMap(
        userLatitude: Double,
        userLongitude: Double,
        radiusKm: Double?
    ): Result<List<LostItem>, DataError.Network> {
        val result = mainApi.getAllMapItems(
            userLatitude = userLatitude,
            userLongitude = userLongitude,
            radiusKm = radiusKm
        )
        return when(result) {
            is Result.Success -> {
               Result.Success(
                   data = result.data.map { it.toModel() }
               )
            }
            is Result.Error -> {
                Result.Error(result.error)
            }
        }
    }

    override suspend fun getDetailedLostItem(itemId: Int): Result<Pair<LostItem, User>, DataError.Network> {
        val resultDTO = mainApi.getDetailedLostItem(itemId = itemId)

        return when (resultDTO) {
            is Result.Success -> {
                Result.Success(
                    data = Pair<LostItem, User>(
                        resultDTO.data.item.toModel(),
                        resultDTO.data.owner.toModel()
                    )
                )
            }

            is Result.Error -> Result.Error(resultDTO.error)
        }
    }

    override suspend fun getFavoriteLostItems(): Result<List<LostItem>, DataError.Network> {
        return when (val result = mainApi.getFavoriteLostItems()) {
            is Result.Success -> {
                Result.Success(
                    data = result.data.map { it.toModel() }
                )
            }

            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getUserCreatedLostItems(): Result<List<LostItem>, DataError.Network> {
        return when (val result = mainApi.getUserCreatedLostItems()) {
            is Result.Success -> {
                Result.Success(
                    data = result.data.map { it.toModel() }
                )
            }

            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun addItemToFavorites(itemId: Int): Result<Unit, DataError.Network> {
        return mainApi.addItemToFavorites(itemId)
    }

    override suspend fun removeItemFromFavorites(itemId: Int): Result<Unit, DataError.Network> {
        return mainApi.removeItemFromFavorites(itemId)
    }

    override suspend fun deleteUserCreatedItem(itemId: Int): Result<Unit, DataError.Network> {
        return mainApi.deleteUserCreatedItem(itemId)
    }

    override suspend fun createChatForItem(
        userId: Int,
        itemId: Int
    ): Result<Chat, DataError.Network> {
        val result = mainApi.createChatForItem(userId, itemId)
        return when (result) {
            is Result.Success -> {
                Result.Success(
                    data = result.data.toModel()
                )
            }

            is Result.Error -> Result.Error(result.error)
        }
    }

}