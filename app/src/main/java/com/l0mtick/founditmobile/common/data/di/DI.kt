package com.l0mtick.founditmobile.common.data.di

import com.l0mtick.founditmobile.MainActivityViewModel
import com.l0mtick.founditmobile.common.data.remote.util.provideHttpClient
import com.l0mtick.founditmobile.common.data.repository.ValidationManagerImpl
import com.l0mtick.founditmobile.common.data.storage.LocalStorageImpl
import com.l0mtick.founditmobile.common.domain.repository.LocalStorage
import com.l0mtick.founditmobile.common.domain.repository.ValidationManager
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {

    single<HttpClient> { provideHttpClient() }

    single<ValidationManager> { ValidationManagerImpl() }

    single<LocalStorage> { LocalStorageImpl(context = get()) }

    viewModel {
        MainActivityViewModel(authRepository = get())
    }

}