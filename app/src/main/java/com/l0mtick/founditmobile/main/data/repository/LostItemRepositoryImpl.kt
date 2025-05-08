package com.l0mtick.founditmobile.main.data.repository

import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.data.util.toModel
import com.l0mtick.founditmobile.main.domain.model.LostItem
import com.l0mtick.founditmobile.main.domain.model.PaginatedData
import com.l0mtick.founditmobile.main.domain.repository.LostItemRepository
import com.l0mtick.founditmobile.main.domain.repository.MainApi

class LostItemRepositoryImpl(private val mainApi: MainApi): LostItemRepository {

    override suspend fun searchLostItems(
        searchQuery: String?,
        categoryIds: List<Int>?,
        userLatitude: Double,
        userLongitude: Double,
        radiusKm: Double?,
        afterId: Int?,
        limit: Int?
    ): Result<PaginatedData<LostItem>, DataError.Network> {
        val resultDTO = mainApi.searchLostItems(
            searchQuery = searchQuery,
            categoryIds = categoryIds,
            userLatitude = userLatitude,
            userLongitude = userLongitude,
            radiusKm = radiusKm,
            afterId = afterId,
            limit = limit
        )

        return when (resultDTO) {
            is Result.Success -> {
                val domainData = resultDTO.data.toModel({it.toModel()})
                Result.Success(domainData)
            }
            is Result.Error -> {
                Result.Error(resultDTO.error)
            }
        }
    }

}