package com.minerdev.faultlocalization.base

import androidx.lifecycle.ViewModel
import com.minerdev.faultlocalization.model.Item

open class ItemViewModel<T : Item>(private val repository: ItemRepository<T>) : ViewModel() {
    val allItems = repository.allItems
    val item = repository.item

    val itemStates = repository.itemStates
    val itemTypes = repository.itemTypes

    fun loadItemsStatesAndTypes() {
        repository.loadItemStatesAndTypes()
    }

    fun loadItem(id: Int) {
        repository.loadItem(id)
    }

    fun loadItems(personId: String = "0", group1: Int = 0, group2: Int = 0) {
        repository.loadItems(personId, group1, group2)
    }

    fun addItem(item: T, onResponse: (response: String?) -> Unit = {}) {
        repository.addItem(item, onResponse)
    }

    fun modifyItem(item: T, onResponse: (response: String?) -> Unit = {}) {
        repository.modifyItem(item, onResponse)
    }

    fun modifyItemState(id: Int, state: Byte, onResponse: (response: String?) -> Unit = {}) {
        repository.modifyItemState(id, state, onResponse)
    }

    fun deleteItem(id: Int, onResponse: (response: String?) -> Unit = {}) {
        repository.deleteItem(id, onResponse)
    }
}