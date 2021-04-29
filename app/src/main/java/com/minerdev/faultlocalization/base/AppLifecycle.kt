package com.minerdev.faultlocalization.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.minerdev.faultlocalization.utils.Constants

class AppLifecycle : Application.ActivityLifecycleCallbacks {
    var appStatus = AppStatus.BACKGROUND
        private set

    enum class AppStatus {
        DESTROYED, BACKGROUND, RETURN_TO_FOREGROUND, FOREGROUND
    }

    private var running = 0

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.d(Constants.TAG, "onActivityCreated " + activity.localClassName)
    }

    override fun onActivityStarted(activity: Activity) {
        Log.d(Constants.TAG, "onActivityStarted " + activity.localClassName)

        if (++running == 1) {
            appStatus = AppStatus.RETURN_TO_FOREGROUND

        } else if (running > 1) {
            appStatus = AppStatus.FOREGROUND
        }
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d(Constants.TAG, "onActivityResumed " + activity.localClassName)
    }

    override fun onActivityPaused(activity: Activity) {
        Log.d(Constants.TAG, "onActivityPaused " + activity.localClassName)
    }

    override fun onActivityStopped(activity: Activity) {
        Log.d(Constants.TAG, "onActivityStopped " + activity.localClassName)

        if (--running == 0) {
            appStatus = AppStatus.BACKGROUND
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.d(Constants.TAG, "onActivitySaveInstanceState " + activity.localClassName)
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d(Constants.TAG, "onActivityDestroyed " + activity.localClassName)
        if (activity.localClassName == "view.activity.MainActivity") {
            appStatus = AppStatus.DESTROYED
        }
    }
}