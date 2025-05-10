package com.l0mtick.founditmobile.main.data.remote.api

import com.l0mtick.founditmobile.common.data.remote.api.BaseApiService
import com.l0mtick.founditmobile.common.data.remote.dto.UserDTO
import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.domain.repository.LocalStorage
import com.l0mtick.founditmobile.main.data.remote.dto.LostItemDTO
import com.l0mtick.founditmobile.main.data.remote.responses.CategoriesResponse
import com.l0mtick.founditmobile.main.data.remote.responses.ChatsResponse
import com.l0mtick.founditmobile.main.data.remote.responses.DetailedLostItemResponse
import com.l0mtick.founditmobile.main.data.remote.responses.PaginatedResponse
import com.l0mtick.founditmobile.main.data.remote.responses.UsersResponse
import com.l0mtick.founditmobile.main.domain.repository.MainApi
import io.ktor.client.HttpClient

class MainApiImpl(httpClient: HttpClient, localStorage: LocalStorage) : MainApi,
    BaseApiService(httpClient, localStorage) {

    override suspend fun getAllCategories(): Result<CategoriesResponse, DataError.Network> {
        return getAuth<CategoriesResponse>(
            path = "user/categories",
        )
    }

    override suspend fun getPopularCategories(): Result<CategoriesResponse, DataError.Network> {
        return getAuth<CategoriesResponse>(
            path = "user/categories/popular",
        )
    }

    override suspend fun getTopLevelUsers(): Result<UsersResponse, DataError.Network> {
        return getAuth<UsersResponse>(
            path = "user/users/top"
        )
    }

    override suspend fun getCurrentUserProfile(): Result<UserDTO, DataError.Network> {
        return getAuth<UserDTO>(
            path = "user/users/me"
        )
    }

    override suspend fun getCurrentUserFavoriteCount(): Result<Int, DataError.Network> {
        return getAuth<Int>(
            path = "user/items/favorite/count"
        )
    }

    override suspend fun getCurrentUserChats(): Result<ChatsResponse, DataError.Network> {
        return getAuth<ChatsResponse>(
            path = "user/chats"
        )
    }

    override suspend fun searchLostItems(
        searchQuery: String?,
        categoryIds: List<Int>?,
        userLatitude: Double,
        userLongitude: Double,
        radiusKm: Double?,
        afterId: Int?,
        limit: Int?
    ): Result<PaginatedResponse<LostItemDTO>, DataError.Network> {
        return getAuth(
            path = "user/items/search",
            params = {
                append("latitude", userLatitude.toString())
                append("longitude", userLongitude.toString())

                searchQuery?.let { if (it.isNotBlank()) append("query", it) }
                categoryIds?.takeIf { it.isNotEmpty() }?.let { ids ->
                    append("categoryIds", ids.joinToString(","))
                }
                radiusKm?.let { append("radiusKm", it.toString()) }
                afterId?.let { append("afterId", it.toString()) }
                limit?.let { append("limit", it.toString()) }
            }
        )
    }

    override suspend fun getDetailedLostItem(itemId: Int): Result<DetailedLostItemResponse, DataError.Network> {
        return getAuth(
            path = "user/item",
            params = {
                append("id", itemId.toString())
            }
        )
    }
}