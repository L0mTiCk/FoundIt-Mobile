package com.l0mtick.founditmobile.main.data.di

import com.l0mtick.founditmobile.main.data.remote.api.MainApiImpl
import com.l0mtick.founditmobile.main.data.repository.CategoriesRepositoryImpl
import com.l0mtick.founditmobile.main.data.repository.ChatRepositoryImpl
import com.l0mtick.founditmobile.main.data.repository.LocationServiceImpl
import com.l0mtick.founditmobile.main.data.repository.LostItemRepositoryImpl
import com.l0mtick.founditmobile.main.data.repository.UsersRepositoryImpl
import com.l0mtick.founditmobile.main.domain.repository.CategoriesRepository
import com.l0mtick.founditmobile.main.domain.repository.ChatRepository
import com.l0mtick.founditmobile.main.domain.repository.LocationService
import com.l0mtick.founditmobile.main.domain.repository.LostItemRepository
import com.l0mtick.founditmobile.main.domain.repository.MainApi
import com.l0mtick.founditmobile.main.domain.repository.UsersRepository
import com.l0mtick.founditmobile.main.presentation.MainScreenViewModel
import com.l0mtick.founditmobile.main.presentation.additem.AddItemViewModel
import com.l0mtick.founditmobile.main.presentation.chat.ChatViewModel
import com.l0mtick.founditmobile.main.presentation.home.HomeViewModel
import com.l0mtick.founditmobile.main.presentation.inbox.InboxViewModel
import com.l0mtick.founditmobile.main.presentation.lostitemdetails.LostItemDetailsViewModel
import com.l0mtick.founditmobile.main.presentation.profile.ProfileViewModel
import com.l0mtick.founditmobile.main.presentation.search.SearchViewModel
import com.l0mtick.founditmobile.main.presentation.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {

    single<MainApi> {
        MainApiImpl(
            httpClient = get(),
            localStorage = get(),
            connectivityObserver = get()
        )
    }

    single<CategoriesRepository> { CategoriesRepositoryImpl(api = get(), localStorage = get()) }

    single<UsersRepository> { UsersRepositoryImpl(mainApi = get(), localStorage = get()) }

    single<ChatRepository> { ChatRepositoryImpl(mainApi = get()) }

    single<LostItemRepository> { LostItemRepositoryImpl(mainApi = get()) }

    single<LocationService> { LocationServiceImpl(application = get()) }

    viewModel {
        MainScreenViewModel(locationService = get(), userSessionManager = get())
    }

    viewModel {
        HomeViewModel(categoriesRepository = get(), usersRepository = get())
    }

    viewModel {
        ProfileViewModel(usersRepository = get())
    }

    viewModel {
        SearchViewModel(
            categoriesRepository = get(),
            lostItemRepository = get(),
            locationService = get(),
            savedStateHandle = get()
        )
    }

    viewModel {
        LostItemDetailsViewModel(
            itemRepository = get(),
            userSessionManager = get(),
            savedStateHandle = get()
        )
    }

    viewModel {
        InboxViewModel(chatRepository = get())
    }

    viewModel {
        ChatViewModel()
    }

    viewModel {
        SettingsViewModel(userPreferencesRepository = get())
    }

    viewModel {
        AddItemViewModel(validator = get())
    }

}