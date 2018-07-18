package com.projectbox.footballmatchschedule

import android.app.Application
import org.koin.android.ext.android.startKoin
import timber.log.Timber

/**
 * Created by adinugroho
 */
class App: Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.IS_TESTING)
            Timber.plant(Timber.DebugTree())

        startKoin(this, listOf(KoinModules().getModules()))
    }
}