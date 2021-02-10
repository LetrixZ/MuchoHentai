package com.letrix.muchohentai.app

import android.app.Application
import com.letrix.muchohentai.app.util.TimberTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(TimberTree())
        }
    }
}