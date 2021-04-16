package com.minerdev.faultlocalization.viewmodel

import androidx.lifecycle.ViewModel
import com.minerdev.faultlocalization.repository.LoginLogRepository

class LoginLogViewModel : ViewModel(){
    private val repository = LoginLogRepository()
    val logs = repository.logs

    fun loadLoginLogs(id: Int) {
        repository.loadLoginLogs(id)
    }
}