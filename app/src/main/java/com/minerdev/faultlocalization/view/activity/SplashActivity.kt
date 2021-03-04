package com.minerdev.faultlocalization.view.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.ActivitySplashBinding
import com.minerdev.faultlocalization.utils.Constants.BASE_URL
import com.minerdev.faultlocalization.utils.Constants.TAG
import com.minerdev.faultlocalization.utils.Constants.TOKEN

class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        BASE_URL = getString(R.string.local_server_dns)

        setupButtons()

        binding.btnLogin.visibility = View.GONE
        binding.btnRegister.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()

        if (!checkInternetConnection()) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("友情提示")
            builder.setMessage("未检测到网络，请检查网络连接状态。")
            builder.setIcon(R.drawable.ic_round_error_24)
            builder.setPositiveButton("确认") { _, _ -> finish() }
            val alertDialog = builder.create()
            alertDialog.show()

        } else if (checkLoginStatus()) {
            val sharedPreferences = getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
            val id = sharedPreferences.getString("id", "") ?: ""
            val token = sharedPreferences.getString("token", "") ?: ""
            TOKEN = token
            Log.d(TAG, "login : $id")
            Log.d(TAG, "login : $token")

            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }, 1000)

        } else {
            binding.btnLogin.visibility = View.VISIBLE
            binding.btnRegister.visibility = View.VISIBLE
        }
    }

    private fun checkLoginStatus(): Boolean {
        val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        return sharedPreferences != null && sharedPreferences.contains("id")
    }

    private fun checkInternetConnection(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        return activeNetwork != null
    }

    private fun setupButtons() {
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}