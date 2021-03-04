package com.minerdev.faultlocalization.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minerdev.faultlocalization.repository.EquipmentRepository
import com.minerdev.faultlocalization.viewmodel.EquipmentViewModel

class EquipmentViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(EquipmentViewModel::class.java)) {
            EquipmentViewModel(EquipmentRepository()) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}