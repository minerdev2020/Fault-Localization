package com.minerdev.faultlocalization.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minerdev.faultlocalization.model.Item

open class ItemViewModel<T : Item>(private val repository: Repository<T>) : ViewModel() {
    val allItems: MutableLiveData<List<T>> = repository.allItems
    val item: MutableLiveData<T> = repository.item

    fun loadItem(id: Int) {
        repository.loadItem(id)
    }

    fun loadItems() {
        repository.loadItems()
    }

    fun addItem(item: T) {
        repository.addItem(item)
    }

    fun modifyItems(item: T) {
        repository.modifyItem(item)
    }

    fun modifyItemState(id: Int, state: Byte) {
        repository.modifyItemState(id, state)
    }

    fun deleteItem(id: Int) {
        repository.deleteItem(id)
    }
}