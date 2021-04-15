package com.minerdev.faultlocalization.repository

import androidx.lifecycle.MutableLiveData
import com.minerdev.faultlocalization.model.LoginLog

class LoginLogRepository {
    val logs = MutableLiveData<List<LoginLog>>()

    fun loadLogs(id: Int) {

    }
}