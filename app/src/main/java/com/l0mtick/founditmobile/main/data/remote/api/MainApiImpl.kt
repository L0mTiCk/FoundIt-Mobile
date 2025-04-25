package com.l0mtick.founditmobile.main.data.remote.api

import com.l0mtick.founditmobile.common.data.remote.api.BaseApiService
import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.domain.repository.LocalStorage
import com.l0mtick.founditmobile.main.data.remote.responses.CategoriesResponse
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
}