package com.l0mtick.founditmobile.main.data.repository

import android.content.Context
import android.net.Uri
import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.domain.repository.LocalStorage
import com.l0mtick.founditmobile.main.data.remote.requests.CreateItemRequest
import com.l0mtick.founditmobile.main.domain.repository.AddItemRepository
import com.l0mtick.founditmobile.main.domain.repository.MainApi
import java.io.ByteArrayOutputStream
import java.io.InputStream

class AddItemRepositoryImpl(
    private val mainApi: MainApi,
    private val localStorage: LocalStorage,
    private val context: Context
): AddItemRepository {
    override suspend fun getUserLevel(): Int? {
        return when(val result = mainApi.getMyLevel()) {
            is Result.Success -> {
                localStorage.setLevel(result.data)
                result.data
            }
            is Result.Error -> {
                localStorage.getLevel()
            }
        }
    }
    
    override suspend fun createItem(
        latitude: Double,
        longitude: Double,
        title: String,
        description: String,
        expiresAt: Long,
        categoryIds: List<Int>
    ): Result<Int, DataError.Network> {
        val request = CreateItemRequest(
            latitude = latitude,
            longitude = longitude,
            title = title,
            description = description,
            expiresAt = expiresAt,
            photoUrls = emptyList(),
            categoryIds = categoryIds
        )
        return mainApi.createItem(request)
    }
    
    override suspend fun uploadItemPhoto(itemId: Int, uri: Uri): Result<String, DataError.Network> {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val byteArray = inputStream?.use { stream ->
                val buffer = ByteArrayOutputStream()
                val data = ByteArray(1024)
                var nRead: Int
                while (stream.read(data, 0, data.size).also { nRead = it } != -1) {
                    buffer.write(data, 0, nRead)
                }
                buffer.toByteArray()
            }
            
            if (byteArray != null) {
                val fileName = "photo_${System.currentTimeMillis()}.jpg"
                mainApi.uploadItemPhoto(itemId, byteArray, fileName)
            } else {
                Result.Error(DataError.Network.UNKNOWN)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
}