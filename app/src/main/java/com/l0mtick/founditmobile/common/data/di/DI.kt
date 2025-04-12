package com.l0mtick.founditmobile.common.data.di

import com.l0mtick.founditmobile.MainActivityViewModel
import com.l0mtick.founditmobile.common.data.repository.ValidationManagerImpl
import com.l0mtick.founditmobile.common.domain.repository.ValidationManager
import com.l0mtick.founditmobile.start.presentation.login.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {

    single<ValidationManager> { ValidationManagerImpl() }

    viewModel {
        MainActivityViewModel()
    }

    viewModel {
        LoginViewModel()
    }

}