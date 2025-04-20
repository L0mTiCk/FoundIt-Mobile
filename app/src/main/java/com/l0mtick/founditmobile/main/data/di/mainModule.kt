package com.l0mtick.founditmobile.main.data.di

import com.l0mtick.founditmobile.main.presentation.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {

    viewModel {
        HomeViewModel()
    }

}