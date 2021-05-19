package com.minerdev.faultlocalization.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minerdev.faultlocalization.repository.RequestRepository
import com.minerdev.faultlocalization.retrofit.service.RequestService
import com.minerdev.faultlocalization.viewmodel.RequestViewModel

class RequestViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RequestViewModel::class.java)) {
            RequestViewModel(RequestRepository(RequestService)) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}