package com.minerdev.faultlocalization.utils

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.minerdev.faultlocalization.retrofit.service.AuthService
import com.minerdev.faultlocalization.utils.Constants.APPLICATION
import com.minerdev.faultlocalization.utils.Constants.ID
import com.minerdev.faultlocalization.utils.Constants.TAG
import com.minerdev.faultlocalization.utils.Constants.TOKEN
import com.minerdev.faultlocalization.utils.Constants.TYPE_ID
import com.minerdev.faultlocalization.utils.Constants.USER_ID
import com.minerdev.faultlocalization.view.activity.SplashActivity
import org.json.JSONObject

object AppHelper {
    fun login(
        userId: String,
        userPw: String,
        onAcceptance: () -> Unit,
        onRejection: () -> Unit
    ) {
        AuthService.login(userId, userPw,
            { _: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "tryLogin response : " + jsonResponse.getString("message"))

                val data = JSONObject(jsonResponse.getString("data"))
                Log.d(TAG, "tryLogin response : " + jsonResponse.getString("data"))

                val sharedPreferences =
                    APPLICATION.getSharedPreferences("login", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("id", data.getInt("id").toString())
                editor.putString("user_id", data.getString("user_id"))
                editor.putString("type_id", data.getString("type_id"))
                editor.putString("token", data.getString("token"))
                editor.apply()

                ID = data.getInt("id").toString()
                USER_ID = data.getString("user_id")
                TYPE_ID = data.getString("type_id")
                TOKEN = data.getString("token")

                onAcceptance()
            },
            { code: Int, response: String ->
                val data = JSONObject(response)
                Log.d(TAG, "tryLogin response : " + data.getString("message"))
                when (code) {
                    400 -> Toast.makeText(APPLICATION, "账号或密码有误！", Toast.LENGTH_SHORT).show()
                    401 -> Toast.makeText(APPLICATION, "该账号已登录！", Toast.LENGTH_SHORT).show()
                    404 -> Toast.makeText(APPLICATION, "账号不存在！", Toast.LENGTH_SHORT).show()
                    else -> {
                    }
                }

                onRejection()
            },
            { error: Throwable ->
                Log.d(TAG, "tryLogin error : " + error.localizedMessage)
            }
        )
    }

    fun logout(onAcceptance: () -> Unit, onRejection: () -> Unit) {
        val sharedPreferences = APPLICATION.getSharedPreferences("login", MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", "") ?: ""
        Log.d(TAG, "logout : $userId")

        AuthService.logout(userId,
            { _: Int, response: String ->
                val data = JSONObject(response)
                Log.d(TAG, "logout response : " + data.getString("message"))
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()

                onAcceptance()
            },
            { code: Int, response: String ->
                val data = JSONObject(response)
                Log.d(TAG, "logout response : " + data.getString("message"))
                when (code) {
                    401 -> {
                        Toast.makeText(APPLICATION, "该账号已注销！", Toast.LENGTH_SHORT).show()
                        val editor = sharedPreferences.edit()
                        editor.clear()
                        editor.apply()
                    }
                    404 -> {
                        Toast.makeText(APPLICATION, "该账号不存在！", Toast.LENGTH_SHORT).show()
                        val editor = sharedPreferences.edit()
                        editor.clear()
                        editor.apply()
                    }
                    else -> {
                    }
                }

                onRejection()
            },
            { error: Throwable ->
                Log.d(TAG, "logout error : " + error.localizedMessage)
            }
        )
    }

    fun register(
        userId: String,
        userPw: String,
        name: String,
        phone: String,
        typeId: Int,
        onAcceptance: () -> Unit,
        onRejection: () -> Unit
    ) {
        AuthService.register(userId, userPw, name, phone, typeId,
            { _: Int, response: String ->
                val data = JSONObject(response)
                Log.d(TAG, "tryRegister response : " + data.getString("message"))

                onAcceptance()
            },
            { code: Int, response: String ->
                val data = JSONObject(response)
                Log.d(TAG, "tryRegister response : " + data.getString("message"))
                when (code) {
                    409 -> Toast.makeText(APPLICATION, "该用户已存在！", Toast.LENGTH_SHORT).show()
                    else -> {
                    }
                }

                onRejection()
            },
            { error: Throwable ->
                Log.d(TAG, "tryRegister error : " + error.localizedMessage)
            }
        )
    }

    fun checkTokenResponse(code: Int) {
        val onInvalidToken = {
            val intent = Intent(APPLICATION, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(APPLICATION, intent, null)
        }

        when (code) {
            401 -> {
                Log.d(TAG, "无效的Token！")
                Toast.makeText(APPLICATION, "无效的Token！请重新登录！", Toast.LENGTH_SHORT).show()
                logout(onInvalidToken, onInvalidToken)
            }
            419 -> {
                Log.d(TAG, "该Token已过期！请重新登录！")
                Toast.makeText(APPLICATION, "该Token已过期！请重新登录！", Toast.LENGTH_SHORT).show()
                logout(onInvalidToken, onInvalidToken)
            }
            else -> {
            }
        }
    }
}