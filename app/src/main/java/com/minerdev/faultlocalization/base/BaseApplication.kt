package com.minerdev.faultlocalization.base

import android.app.Application

class BaseApplication : Application() {
    val appLifecycle = AppLifecycle()
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(appLifecycle)
    }
}