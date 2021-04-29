package com.minerdev.faultlocalization.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minerdev.faultlocalization.repository.AlertRepository
import com.minerdev.faultlocalization.retrofit.service.AlertService
import com.minerdev.faultlocalization.viewmodel.AlertViewModel

class AlertViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            AlertViewModel(AlertRepository(AlertService)) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}