package com.konrad.hiringtest

import android.app.Application
import timber.log.Timber

class HiringTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}