package com.l0mtick.founditmobile.main.domain.repository

import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.domain.model.User

interface UsersRepository {

    suspend fun getTopLevelUsers(): Result<List<User>?, DataError.Network>
    suspend fun getMe(): Result<User, DataError.Network>
    suspend fun getMyFavoriteCount(): Result<Int, DataError.Network>
    suspend fun getLocalMe(): Result<User, DataError>

}