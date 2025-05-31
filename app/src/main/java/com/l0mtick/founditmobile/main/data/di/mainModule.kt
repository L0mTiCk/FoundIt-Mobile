package com.l0mtick.founditmobile.main.data.di

import com.l0mtick.founditmobile.main.data.remote.api.MainApiImpl
import com.l0mtick.founditmobile.main.data.remote.websocket.ChatWebSocketClient
import com.l0mtick.founditmobile.main.data.remote.websocket.ChatWebSocketClientImpl
import com.l0mtick.founditmobile.main.data.repository.AddItemRepositoryImpl
import com.l0mtick.founditmobile.main.data.repository.CategoriesRepositoryImpl
import com.l0mtick.founditmobile.main.data.repository.ChatRepositoryImpl
import com.l0mtick.founditmobile.main.data.repository.LocationServiceImpl
import com.l0mtick.founditmobile.main.data.repository.LostItemRepositoryImpl
import com.l0mtick.founditmobile.main.data.repository.UsersRepositoryImpl
import com.l0mtick.founditmobile.main.domain.repository.AddItemRepository
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
import com.l0mtick.founditmobile.main.presentation.useritems.UserItemsViewModel
import kotlinx.serialization.json.Json
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

    single<UsersRepository> { UsersRepositoryImpl(mainApi = get(), localStorage = get(), context = get()) }

    single<ChatRepository> { ChatRepositoryImpl(mainApi = get(), chatWebSocketClient = get()) }

    single<LostItemRepository> { LostItemRepositoryImpl(mainApi = get()) }

    single<LocationService> { LocationServiceImpl(application = get()) }

    single<AddItemRepository> {
        AddItemRepositoryImpl(
            mainApi = get(),
            localStorage = get(),
            context = get()
        )
    }

    single<ChatWebSocketClient> {
        val json = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }
        ChatWebSocketClientImpl(httpClient = get(), json = json, localStorage = get())
    }

    viewModel {
        MainScreenViewModel(locationService = get(), userSessionManager = get())
    }

    viewModel {
        HomeViewModel(categoriesRepository = get(), usersRepository = get())
    }

    viewModel {
        ProfileViewModel(usersRepository = get(), snackbarManager = get())
    }

    viewModel {
        SearchViewModel(
            categoriesRepository = get(),
            lostItemRepository = get(),
            locationService = get(),
            snackbarManager = get(),
            savedStateHandle = get()
        )
    }

    viewModel {
        LostItemDetailsViewModel(
            itemRepository = get(),
            userSessionManager = get(),
            snackbarManager = get(),
            savedStateHandle = get()
        )
    }

    viewModel {
        InboxViewModel(chatRepository = get(), snackbarManager = get())
    }

    viewModel {
        ChatViewModel(
            chatRepository = get(),
            snackbarManager = get(),
            savedStateHandle = get()
        )
    }

    viewModel {
        SettingsViewModel(userPreferencesRepository = get())
    }

    viewModel {
        AddItemViewModel(
            validator = get(),
            categoriesRepository = get(),
            addItemRepository = get(),
            locationService = get(),
            snackbarManager = get()
        )
    }

    viewModel {
        UserItemsViewModel(
            lostItemRepository = get(),
            snackbarManager = get(),
            savedStateHandle = get()
        )
    }
}