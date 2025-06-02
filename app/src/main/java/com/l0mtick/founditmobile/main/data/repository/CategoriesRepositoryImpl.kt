package com.l0mtick.founditmobile.main.data.repository

import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.domain.repository.LocalStorage
import com.l0mtick.founditmobile.main.data.remote.responses.CategoriesResponse
import com.l0mtick.founditmobile.main.data.util.toModel
import com.l0mtick.founditmobile.main.domain.model.Category
import com.l0mtick.founditmobile.main.domain.repository.CategoriesRepository
import com.l0mtick.founditmobile.main.domain.repository.MainApi

class CategoriesRepositoryImpl(private val api: MainApi, private val localStorage: LocalStorage) :
    CategoriesRepository {

    override suspend fun getAllCategories(): Result<List<Category>, DataError.Network> {
        return handleCategoryResult(api.getAllCategories())
    }

    override suspend fun getPopularCategories(): Result<List<Category>, DataError.Network> {
        return handleCategoryResult(api.getPopularCategories())
    }

    private suspend fun handleCategoryResult(
        response: Result<CategoriesResponse, DataError.Network>
    ): Result<List<Category>, DataError.Network> {
        val languageCode = localStorage.getAppLanguage()
        return when (response) {
            is Result.Error -> Result.Error(response.error)
            is Result.Success -> Result.Success(
                response.data.categories.map { it.toModel(languageCode) }
            )
        }
    }
}