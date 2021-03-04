package com.minerdev.faultlocalization.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minerdev.faultlocalization.base.ItemViewModel
import com.minerdev.faultlocalization.repository.PersonRepository
import com.minerdev.faultlocalization.viewmodel.PersonViewModel

class PersonViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PersonViewModel::class.java)) {
            PersonViewModel(PersonRepository()) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}