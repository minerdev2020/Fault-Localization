package com.minerdev.faultlocalization.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.minerdev.faultlocalization.retrofit.service.LoginLogService
import com.minerdev.faultlocalization.utils.AppHelper
import com.minerdev.faultlocalization.utils.Constants.TAG
import org.json.JSONObject

class LoginLogRepository {
    val logs = MutableLiveData<String>()

    fun loadLoginLogs(id: Int) {
        LoginLogService.getLoginLogs(
            id,
            { _: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "loadLoginLogs response : " + jsonResponse.getString("message"))
                Log.d(TAG, "loadLoginLogs response : " + jsonResponse.getString("data"))
                logs.postValue(jsonResponse.getString("data"))
            },
            { code: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "loadLoginLogs response : " + jsonResponse.getString("message"))
                AppHelper.checkTokenResponse(code)
            },
            { error: Throwable ->
                Log.d(TAG, "loadLoginLogs error : " + error.localizedMessage)
            })
    }

}