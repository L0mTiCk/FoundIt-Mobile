package com.l0mtick.founditmobile.common.data.di

import com.l0mtick.founditmobile.MainActivityViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {

    viewModel {
        MainActivityViewModel()
    }

}