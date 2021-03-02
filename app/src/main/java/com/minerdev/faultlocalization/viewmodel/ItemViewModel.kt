package com.minerdev.faultlocalization.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minerdev.faultlocalization.Repository
import com.minerdev.faultlocalization.model.Item
import kotlinx.serialization.InternalSerializationApi
import kotlin.reflect.KClass

open class ItemViewModel<T : Item>(itemType: KClass<T>) : ViewModel() {
    val allItems : MutableLiveData<List<T>>
    val item : MutableLiveData<T>

    private val repository : Repository<T> = Repository(itemType)

    init {
        allItems = repository.allItems
        item = repository.item
    }

    @InternalSerializationApi
    fun loadItems() {
        repository.loadItems()
    }

    fun addItem(item: T) {
        repository.addItem(item, { })
    }

    fun modifyItems(item: T) {
        repository.modifyItem(item, { })
    }

    fun modifyItemState(id: Int, state: Byte) {
        repository.modifyItemState(id, state)
    }
}