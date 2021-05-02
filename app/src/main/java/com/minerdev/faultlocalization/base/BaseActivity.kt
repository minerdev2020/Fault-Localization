package com.minerdev.faultlocalization.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.minerdev.faultlocalization.service.NotificationService
import com.minerdev.faultlocalization.utils.AppHelper
import com.minerdev.faultlocalization.utils.Constants.TOKEN_VALID
import com.minerdev.faultlocalization.view.activity.SplashActivity

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TOKEN_VALID.observe(this, {
            checkToken(it)
        })
    }

    private fun checkToken(isValid: Boolean) {
        if (!isValid) {
            val onInvalidToken = {
                val intent = Intent(this, SplashActivity::class.java)
                this.startActivity(intent)
                ActivityCompat.finishAffinity(this)
            }

            stopService(
                Intent(
                    applicationContext,
                    NotificationService::class.java
                )
            )
            AppHelper.logout(onInvalidToken, onInvalidToken)
        }
    }
}