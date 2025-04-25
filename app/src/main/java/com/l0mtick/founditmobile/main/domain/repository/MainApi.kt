package com.l0mtick.founditmobile.main.domain.repository

import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.data.remote.responses.CategoriesResponse

interface MainApi {

    suspend fun getAllCategories(): Result<CategoriesResponse, DataError.Network>

    suspend fun getPopularCategories():  Result<CategoriesResponse, DataError.Network>

}