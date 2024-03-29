package com.minerdev.faultlocalization.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minerdev.faultlocalization.repository.EquipmentRepository
import com.minerdev.faultlocalization.repository.RequestRepository
import com.minerdev.faultlocalization.retrofit.service.EquipmentService
import com.minerdev.faultlocalization.retrofit.service.RequestService
import com.minerdev.faultlocalization.viewmodel.EquipmentViewModel

class EquipmentViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(EquipmentViewModel::class.java)) {
            EquipmentViewModel(
                RequestRepository(RequestService),
                EquipmentRepository(EquipmentService)
            ) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}