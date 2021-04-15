package com.minerdev.faultlocalization.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minerdev.faultlocalization.repository.EquipmentRepository
import com.minerdev.faultlocalization.repository.SensorRepository
import com.minerdev.faultlocalization.retrofit.service.EquipmentService
import com.minerdev.faultlocalization.retrofit.service.SensorService
import com.minerdev.faultlocalization.viewmodel.EquipmentModifyViewModel

class EquipmentModifyViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(EquipmentModifyViewModel::class.java)) {
            EquipmentModifyViewModel(
                SensorRepository(SensorService),
                EquipmentRepository(EquipmentService)
            ) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}