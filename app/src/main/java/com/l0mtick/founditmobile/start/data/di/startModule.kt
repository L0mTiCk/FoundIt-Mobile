package com.l0mtick.founditmobile.start.data.di

import com.l0mtick.founditmobile.start.data.remote.api.AuthApiImpl
import com.l0mtick.founditmobile.start.data.repository.AuthRepositoryImpl
import com.l0mtick.founditmobile.start.domain.repository.AuthApi
import com.l0mtick.founditmobile.start.domain.repository.AuthRepository
import com.l0mtick.founditmobile.start.presentation.login.LoginViewModel
import com.l0mtick.founditmobile.start.presentation.phoneverify.PhoneVerificationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val startModule = module {
    single<AuthApi> {
        AuthApiImpl(
            httpClient = get(),
            localStorage = get(),
            connectivityObserver = get()
        )
    }

    single<AuthRepository> { AuthRepositoryImpl(localStorage = get(), authApi = get()) }

    viewModel {
        LoginViewModel(validator = get(), authRepository = get())
    }

    viewModel {
        PhoneVerificationViewModel(authRepository = get(), savedStateHandle = get())
    }
}