package com.minerdev.faultlocalization.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.base.AppLifecycle
import com.minerdev.faultlocalization.base.BaseApplication
import com.minerdev.faultlocalization.utils.Constants

class DummyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dummy)

        val appStatus = (application as BaseApplication).appLifecycle.appStatus
        Log.d(Constants.TAG, appStatus.name)

        val intent = intent
        val data = intent.getStringExtra("data")

        val newIntent = if (appStatus == AppLifecycle.AppStatus.DESTROYED) {
            Intent(this, SplashActivity::class.java).apply {
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_LAUNCHER)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

        } else {
            Intent(this, AlertActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        }

        newIntent.putExtra("data", data)
        startActivity(newIntent)
        finish()
    }
}