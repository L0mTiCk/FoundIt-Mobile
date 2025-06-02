package com.l0mtick.founditmobile.main.domain.repository

import com.l0mtick.founditmobile.common.domain.error.DataError
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.domain.model.Category

interface CategoriesRepository {

    suspend fun getAllCategories(): Result<List<Category>, DataError.Network>

    suspend fun getPopularCategories(): Result<List<Category>, DataError.Network>

}