package com.minerdev.faultlocalization.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minerdev.faultlocalization.repository.EquipmentRepository
import com.minerdev.faultlocalization.viewmodel.EquipmentViewModel

class EquipmentViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(EquipmentViewModel::class.java)) {
            EquipmentViewModel(EquipmentRepository(context)) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}