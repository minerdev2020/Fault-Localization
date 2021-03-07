package com.minerdev.faultlocalization.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minerdev.faultlocalization.base.ItemViewModel
import com.minerdev.faultlocalization.repository.PersonRepository
import com.minerdev.faultlocalization.viewmodel.PersonViewModel

class PersonViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PersonViewModel::class.java)) {
            PersonViewModel(PersonRepository(context)) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}