package com.minerdev.faultlocalization.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minerdev.faultlocalization.repository.EquipmentRepository
import com.minerdev.faultlocalization.repository.SensorRepository
import com.minerdev.faultlocalization.viewmodel.EquipmentModifyViewModel

class EquipmentModifyViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(EquipmentModifyViewModel::class.java)) {
            EquipmentModifyViewModel(SensorRepository(context), EquipmentRepository(context)) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}