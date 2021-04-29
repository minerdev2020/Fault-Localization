package com.minerdev.faultlocalization.view.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.ActivitySplashBinding
import com.minerdev.faultlocalization.utils.Constants

class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Constants.APPLICATION = application
        Constants.BASE_URL = getString(R.string.local_server_dns)

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
            val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
            val id = sharedPreferences.getString("id", "") ?: ""
            val userId = sharedPreferences.getString("user_id", "") ?: ""
            val typeId = sharedPreferences.getString("type_id", "") ?: ""
            val token = sharedPreferences.getString("token", "") ?: ""

            Constants.ID = id
            Constants.USER_ID = userId
            Constants.TYPE_ID = typeId
            Constants.TOKEN = token

            Log.d(Constants.TAG, "login : $userId")
            Log.d(Constants.TAG, "login : $typeId")
            Log.d(Constants.TAG, "login : $token")

            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

            val data = getIntent().getStringExtra("data")
            if (data != null) {
                intent.putExtra("data", data)
            }

            startActivity(intent)
            finish()

        } else {
            binding.btnLogin.visibility = View.VISIBLE
            binding.btnRegister.visibility = View.VISIBLE
        }
    }

    private fun checkLoginStatus(): Boolean {
        val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        return sharedPreferences != null && sharedPreferences.contains("user_id")
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