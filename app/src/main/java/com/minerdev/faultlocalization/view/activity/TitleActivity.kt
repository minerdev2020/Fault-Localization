package com.minerdev.faultlocalization.view.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.ActivityTitleBinding
import com.minerdev.faultlocalization.utils.Constants.BASE_URL
import com.minerdev.faultlocalization.utils.Constants.TAG
import com.minerdev.faultlocalization.utils.Constants.TOKEN

class TitleActivity : AppCompatActivity() {
    private val binding by lazy { ActivityTitleBinding.inflate(layoutInflater) }
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        BASE_URL = getString(R.string.local_server_dns)

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
            val token = sharedPreferences.getString("token", "") ?: ""
            TOKEN = token
            Log.d(TAG, "login : $id")
            Log.d(TAG, "login : $token")

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
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
}