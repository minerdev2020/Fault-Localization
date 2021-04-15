package com.minerdev.faultlocalization.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minerdev.faultlocalization.repository.PersonRepository
import com.minerdev.faultlocalization.retrofit.service.PersonService
import com.minerdev.faultlocalization.viewmodel.PersonViewModel

class PersonViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PersonViewModel::class.java)) {
            PersonViewModel(PersonRepository(PersonService)) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}