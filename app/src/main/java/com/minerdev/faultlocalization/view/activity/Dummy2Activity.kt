package com.minerdev.faultlocalization.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.base.AppLifecycle
import com.minerdev.faultlocalization.base.BaseApplication
import com.minerdev.faultlocalization.utils.Constants

class Dummy2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dummy)

        val appStatus = (application as BaseApplication).appLifecycle.appStatus
        Log.d(Constants.TAG, appStatus.name)

        if (appStatus == AppLifecycle.AppStatus.DESTROYED) {
            val intent = Intent(this, SplashActivity::class.java).apply {
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_LAUNCHER)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }

        finish()
    }
}