package com.minerdev.faultlocalization.retrofit.service

import com.minerdev.faultlocalization.retrofit.RetrofitClient
import com.minerdev.faultlocalization.retrofit.api.LoginLogApi
import com.minerdev.faultlocalization.utils.Constants.API_LOGIN_LOG
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object LoginLogService {
    private val client = RetrofitClient.getClient(API_LOGIN_LOG)?.create(LoginLogApi::class.java)

    fun getLoginLogs(
        id: Int,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = client?.getLoginLogs(id) ?: return
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onAcceptance(response.code(), it.toString())
                    }

                } else {
                    response.errorBody()?.let {
                        onRejection(response.code(), it.string())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}