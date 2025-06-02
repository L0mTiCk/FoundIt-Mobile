package com.l0mtick.founditmobile.main.domain.repository

import android.net.Uri
import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result

interface AddItemRepository {
    suspend fun getUserLevel(): Int?
    
    /**
     * Creates a new item without photos and returns its ID
     */
    suspend fun createItem(
        latitude: Double,
        longitude: Double,
        title: String,
        description: String,
        expiresAt: Long,
        categoryIds: List<Int>
    ): Result<Int, DataError.Network>
    
    /**
     * Uploads a photo for a specific item
     * @param itemId ID of the item to attach the photo to
     * @param uri URI of the photo to upload
     * @return Result containing the URL of the uploaded photo
     */
    suspend fun uploadItemPhoto(itemId: Int, uri: Uri): Result<String, DataError.Network>
}