package com.l0mtick.founditmobile.main.domain.repository

import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.data.remote.dto.UserDTO
import com.l0mtick.founditmobile.main.data.remote.responses.CategoriesResponse
import com.l0mtick.founditmobile.main.data.remote.responses.UsersResponse

interface MainApi {

    suspend fun getAllCategories(): Result<CategoriesResponse, DataError.Network>
    suspend fun getPopularCategories():  Result<CategoriesResponse, DataError.Network>

    suspend fun getTopLevelUsers(): Result<UsersResponse, DataError.Network>

    suspend fun getCurrentUserProfile(): Result<UserDTO, DataError.Network>
    suspend fun getCurrentUserFavoriteCount(): Result<Int, DataError.Network>
}