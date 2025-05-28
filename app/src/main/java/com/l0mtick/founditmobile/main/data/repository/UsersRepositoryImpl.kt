package com.l0mtick.founditmobile.main.data.repository

import android.content.Context
import android.net.Uri
import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.domain.repository.LocalStorage
import com.l0mtick.founditmobile.main.data.util.toModel
import com.l0mtick.founditmobile.main.domain.model.User
import com.l0mtick.founditmobile.main.domain.repository.MainApi
import com.l0mtick.founditmobile.main.domain.repository.UsersRepository
import java.io.ByteArrayOutputStream
import java.io.InputStream

class UsersRepositoryImpl(
    private val mainApi: MainApi,
    private val localStorage: LocalStorage,
    private val context: Context
) : UsersRepository {

    override suspend fun getTopLevelUsers(): Result<List<User>?, DataError.Network> {
        val result = mainApi.getTopLevelUsers()
        return when (result) {
            is Result.Success -> {
                Result.Success(
                    data = result.data.users?.map { it.toModel() })
            }

            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getMe(): Result<User, DataError.Network> {
        val result = mainApi.getCurrentUserProfile()
        return when (result) {
            is Result.Success -> {
                Result.Success(
                    data = result.data.toModel()
                )
            }

            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getMyFavoriteCount(): Result<Int, DataError.Network> {
        val result = mainApi.getCurrentUserFavoriteCount()
        return when (result) {
            is Result.Success -> {
                Result.Success(
                    data = result.data
                )
            }

            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getLocalMe(): Result<User, DataError> {
        val username = localStorage.getUsername()
        val profilePictureUrl = localStorage.getProfilePictureUrl()
        return Result.Success(
            data = User(
                id = -1,
                username = username ?: "----",
                profilePictureUrl = profilePictureUrl
            )
        )
    }

    override suspend fun deleteUserProfilePicture(): Result<Unit, DataError.Network> {
        return mainApi.deleteUserProfilePicture()
    }

    override suspend fun updateUserProfilePicture(logoUri: Uri): Result<Unit, DataError.Network> {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(logoUri)
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
                val result = mainApi.uploadUserProfilePicture(byteArray, fileName)
                return when(result) {
                    is Result.Success -> Result.Success(data = Unit)
                    is Result.Error -> Result.Error(result.error)
                }
            } else {
                return Result.Error(DataError.Network.UNKNOWN)
            }
        } catch (e: Exception) {
            return Result.Error(DataError.Network.UNKNOWN)
        }
    }

}