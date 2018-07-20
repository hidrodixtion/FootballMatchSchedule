package com.projectbox.footballmatchschedule

import android.app.Application
import com.facebook.stetho.Stetho
import org.koin.android.ext.android.startKoin
import timber.log.Timber

/**
 * Created by adinugroho
 */
class App: Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.IS_TESTING) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
        }

        startKoin(this, listOf(KoinModules().getModules()))
    }
}