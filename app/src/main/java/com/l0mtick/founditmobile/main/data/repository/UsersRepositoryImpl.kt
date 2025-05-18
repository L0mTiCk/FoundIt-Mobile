package com.l0mtick.founditmobile.main.data.repository

import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.domain.repository.LocalStorage
import com.l0mtick.founditmobile.main.data.util.toModel
import com.l0mtick.founditmobile.main.domain.model.User
import com.l0mtick.founditmobile.main.domain.repository.MainApi
import com.l0mtick.founditmobile.main.domain.repository.UsersRepository

class UsersRepositoryImpl(
    private val mainApi: MainApi,
    private val localStorage: LocalStorage
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

}