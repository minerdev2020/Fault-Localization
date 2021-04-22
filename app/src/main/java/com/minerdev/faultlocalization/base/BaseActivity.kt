package com.minerdev.faultlocalization.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                this.startActivity(intent)
            }

            AppHelper.logout(onInvalidToken, onInvalidToken)
        }
    }
}