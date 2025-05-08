package com.l0mtick.founditmobile.main.domain.repository

import com.l0mtick.founditmobile.common.data.remote.dto.UserDTO
import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.data.remote.dto.LostItemDTO
import com.l0mtick.founditmobile.main.data.remote.responses.CategoriesResponse
import com.l0mtick.founditmobile.main.data.remote.responses.ChatsResponse
import com.l0mtick.founditmobile.main.data.remote.responses.PaginatedResponse
import com.l0mtick.founditmobile.main.data.remote.responses.UsersResponse

interface MainApi {

    suspend fun getAllCategories(): Result<CategoriesResponse, DataError.Network>
    suspend fun getPopularCategories():  Result<CategoriesResponse, DataError.Network>

    suspend fun getTopLevelUsers(): Result<UsersResponse, DataError.Network>

    suspend fun getCurrentUserProfile(): Result<UserDTO, DataError.Network>
    suspend fun getCurrentUserFavoriteCount(): Result<Int, DataError.Network>
    suspend fun getCurrentUserChats(): Result<ChatsResponse, DataError.Network>

    suspend fun searchLostItems(
        searchQuery: String?,
        categoryIds: List<Int>?,
        userLatitude: Double,
        userLongitude: Double,
        radiusKm: Double?,
        afterId: Int?,
        limit: Int?
    ): Result<PaginatedResponse<LostItemDTO>, DataError.Network>

}