package com.minerdev.faultlocalization.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minerdev.faultlocalization.model.Item
import kotlin.reflect.KClass

class ItemViewModelFactory<K : Item>(private val itemType: KClass<K>) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            ItemViewModel(itemType) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}