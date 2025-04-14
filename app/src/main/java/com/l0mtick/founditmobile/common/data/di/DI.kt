package com.l0mtick.founditmobile.common.data.di

import com.l0mtick.founditmobile.MainActivityViewModel
import com.l0mtick.founditmobile.common.data.repository.ValidationManagerImpl
import com.l0mtick.founditmobile.common.data.storage.LocalStorageImpl
import com.l0mtick.founditmobile.common.domain.repository.LocalStorage
import com.l0mtick.founditmobile.common.domain.repository.ValidationManager
import com.l0mtick.founditmobile.start.presentation.login.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {

    single<ValidationManager> { ValidationManagerImpl() }

    single<LocalStorage> { LocalStorageImpl(context = get()) }

    viewModel {
        MainActivityViewModel()
    }

    viewModel {
        LoginViewModel(validator = get())
    }

}