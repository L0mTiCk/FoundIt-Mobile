package com.l0mtick.founditmobile

import android.app.Application
import com.l0mtick.founditmobile.common.data.di.commonModule
import com.l0mtick.founditmobile.start.data.di.authModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class FoundItMobileApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(level = Level.ERROR)
            androidContext(this@FoundItMobileApplication)
            modules(
                commonModule,
                authModule
            )
        }
    }

}