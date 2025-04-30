package com.l0mtick.founditmobile.main.data.di

import com.l0mtick.founditmobile.main.data.remote.api.MainApiImpl
import com.l0mtick.founditmobile.main.data.repository.CategoriesRepositoryImpl
import com.l0mtick.founditmobile.main.domain.repository.CategoriesRepository
import com.l0mtick.founditmobile.main.domain.repository.MainApi
import com.l0mtick.founditmobile.main.presentation.home.HomeViewModel
import com.l0mtick.founditmobile.main.presentation.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {

    single<MainApi> { MainApiImpl(httpClient = get(), localStorage = get()) }

    single<CategoriesRepository> { CategoriesRepositoryImpl(api = get(), localStorage = get()) }

    viewModel {
        HomeViewModel(categoriesRepository = get())
    }

    viewModel {
        ProfileViewModel()
    }

}