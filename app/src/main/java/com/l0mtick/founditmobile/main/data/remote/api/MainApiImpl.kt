package com.l0mtick.founditmobile.main.data.remote.api

import com.l0mtick.founditmobile.common.data.remote.api.BaseApiService
import com.l0mtick.founditmobile.common.data.remote.dto.UserDTO
import com.l0mtick.founditmobile.common.data.remote.request.PushTokenRequest
import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.domain.repository.ConnectivityObserver
import com.l0mtick.founditmobile.common.domain.repository.LocalStorage
import com.l0mtick.founditmobile.main.data.remote.dto.LostItemDTO
import com.l0mtick.founditmobile.main.data.remote.requests.CreateItemRequest
import com.l0mtick.founditmobile.main.data.remote.responses.CategoriesResponse
import com.l0mtick.founditmobile.main.data.remote.responses.ChatsResponse
import com.l0mtick.founditmobile.main.data.remote.responses.DetailedLostItemResponse
import com.l0mtick.founditmobile.main.data.remote.responses.FullDataMessageResponse
import com.l0mtick.founditmobile.main.data.remote.responses.PaginatedResponse
import com.l0mtick.founditmobile.main.data.remote.responses.UsersResponse
import com.l0mtick.founditmobile.main.domain.repository.MainApi
import io.ktor.client.HttpClient
import io.ktor.util.reflect.typeInfo

class MainApiImpl(
    httpClient: HttpClient,
    localStorage: LocalStorage,
    connectivityObserver: ConnectivityObserver
) : MainApi,
    BaseApiService(httpClient, localStorage, connectivityObserver) {

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
    
    override suspend fun getChatMessages(chatId: Int): Result<FullDataMessageResponse, DataError.Network> {
        return getAuth<FullDataMessageResponse>(
            path = "user/messages/$chatId"
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

    override suspend fun sendUserPushToken(token: String): Result<Unit, DataError.Network> {
        val request = PushTokenRequest(token)
        return postAuth(
            path = "user/push-token",
            body = request
        )
    }
    
    override suspend fun createItem(request: CreateItemRequest): Result<Int, DataError.Network> {
        return postAuth(
            path = "user/item",
            body = request
        )
    }
    
    override suspend fun uploadItemPhoto(itemId: Int, photoData: ByteArray, fileName: String): Result<String, DataError.Network> {
        return uploadFileAuth(
            type = typeInfo<String>(),
            path = "image/lost-item",
            fileData = photoData,
            fileName = fileName,
            fieldName = "photo",
            additionalFields = mapOf("itemId" to itemId.toString())
        )
    }
    
    override suspend fun sendMessage(chatId: Int, content: String): Result<Unit, DataError.Network> {
        return postAuth(
            path = "messages/$chatId",
            body = mapOf("content" to content)
        )
    }
}