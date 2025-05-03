package com.l0mtick.founditmobile.main.data.di

import com.l0mtick.founditmobile.main.data.remote.api.MainApiImpl
import com.l0mtick.founditmobile.main.data.repository.CategoriesRepositoryImpl
import com.l0mtick.founditmobile.main.data.repository.UsersRepositoryImpl
import com.l0mtick.founditmobile.main.domain.repository.CategoriesRepository
import com.l0mtick.founditmobile.main.domain.repository.MainApi
import com.l0mtick.founditmobile.main.domain.repository.UsersRepository
import com.l0mtick.founditmobile.main.presentation.chat.ChatViewModel
import com.l0mtick.founditmobile.main.presentation.home.HomeViewModel
import com.l0mtick.founditmobile.main.presentation.inbox.InboxViewModel
import com.l0mtick.founditmobile.main.presentation.lostitemdetails.LostItemDetailsViewModel
import com.l0mtick.founditmobile.main.presentation.profile.ProfileViewModel
import com.l0mtick.founditmobile.main.presentation.search.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {

    single<MainApi> { MainApiImpl(httpClient = get(), localStorage = get()) }

    single<CategoriesRepository> { CategoriesRepositoryImpl(api = get(), localStorage = get()) }

    single<UsersRepository> { UsersRepositoryImpl(get()) }

    viewModel {
        HomeViewModel(categoriesRepository = get(), usersRepository = get())
    }

    viewModel {
        ProfileViewModel(usersRepository = get())
    }

    viewModel {
        SearchViewModel()
    }

    viewModel {
        LostItemDetailsViewModel()
    }

    viewModel {
        InboxViewModel()
    }

    viewModel {
        ChatViewModel()
    }

}