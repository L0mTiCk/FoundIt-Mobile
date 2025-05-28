package com.l0mtick.founditmobile.main.domain.repository

import com.l0mtick.founditmobile.common.data.remote.dto.UserDTO
import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.data.remote.dto.ChatDTO
import com.l0mtick.founditmobile.main.data.remote.dto.LostItemDTO
import com.l0mtick.founditmobile.main.data.remote.requests.CreateItemRequest
import com.l0mtick.founditmobile.main.data.remote.responses.CategoriesResponse
import com.l0mtick.founditmobile.main.data.remote.responses.ChatsResponse
import com.l0mtick.founditmobile.main.data.remote.responses.DetailedLostItemResponse
import com.l0mtick.founditmobile.main.data.remote.responses.FullDataMessageResponse
import com.l0mtick.founditmobile.main.data.remote.responses.PaginatedResponse
import com.l0mtick.founditmobile.main.data.remote.responses.UsersResponse

interface MainApi {

    suspend fun getAllCategories(): Result<CategoriesResponse, DataError.Network>
    suspend fun getPopularCategories():  Result<CategoriesResponse, DataError.Network>

    suspend fun getTopLevelUsers(): Result<UsersResponse, DataError.Network>

    suspend fun getCurrentUserProfile(): Result<UserDTO, DataError.Network>
    suspend fun getCurrentUserFavoriteCount(): Result<Int, DataError.Network>
    suspend fun getCurrentUserChats(): Result<ChatsResponse, DataError.Network>
    suspend fun getChatMessages(chatId: Int): Result<FullDataMessageResponse, DataError.Network>

    suspend fun searchLostItems(
        searchQuery: String?,
        categoryIds: List<Int>?,
        userLatitude: Double,
        userLongitude: Double,
        radiusKm: Double?,
        afterId: Int?,
        limit: Int?,
        date: Long?
    ): Result<PaginatedResponse<LostItemDTO>, DataError.Network>
    suspend fun getDetailedLostItem(itemId: Int): Result<DetailedLostItemResponse, DataError.Network>

    suspend fun sendUserPushToken(token: String): Result<Unit, DataError.Network>
    
    /**
     * Creates a new lost item without photos
     * @return Result containing the created item ID
     */
    suspend fun createItem(request: CreateItemRequest): Result<Int, DataError.Network>
    
    /**
     * Uploads a photo for a specific lost item
     * @param itemId ID of the lost item to attach the photo to
     * @param photoData Binary data of the photo
     * @param fileName Name of the photo file
     * @return Result containing the URL of the uploaded photo
     */
    suspend fun uploadItemPhoto(itemId: Int, photoData: ByteArray, fileName: String): Result<String, DataError.Network>
    
    /**
     * Sends a message to a chat
     * @param chatId ID of the chat
     * @param content Message content
     * @return Result containing the sent message
     */
    suspend fun sendMessage(chatId: Int, content: String): Result<Unit, DataError.Network>

    suspend fun getMyLevel(): Result<Int, DataError.Network>

    suspend fun getFavoriteLostItems(): Result<List<LostItemDTO>, DataError.Network>
    suspend fun getUserCreatedLostItems(): Result<List<LostItemDTO>, DataError.Network>
    suspend fun addItemToFavorites(itemId: Int): Result<Unit, DataError.Network>
    suspend fun removeItemFromFavorites(itemId: Int): Result<Unit, DataError.Network>

    suspend fun deleteUserCreatedItem(itemId: Int): Result<Unit, DataError.Network>

    suspend fun createChatForItem(userId: Int, itemId: Int): Result<ChatDTO, DataError.Network>

}