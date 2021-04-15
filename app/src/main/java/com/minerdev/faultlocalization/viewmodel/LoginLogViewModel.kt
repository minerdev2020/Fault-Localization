package com.minerdev.faultlocalization.viewmodel

import com.minerdev.faultlocalization.repository.LoginLogRepository

class LoginLogViewModel {
    private val repository = LoginLogRepository()
    val logs = repository.logs

    fun loadLogs(id: Int) {
        repository.loadLogs(id)
    }
}