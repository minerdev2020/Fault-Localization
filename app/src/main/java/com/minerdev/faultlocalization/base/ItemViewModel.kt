package com.minerdev.faultlocalization.base

import androidx.lifecycle.ViewModel
import com.minerdev.faultlocalization.model.Item

open class ItemViewModel<T : Item>(private val repository: Repository<T>) : ViewModel() {
    val allItems = repository.allItems
    val item = repository.item

    val itemStates = repository.itemStates
    val itemTypes = repository.itemTypes

    fun loadItemsStatesAndTypes() {
        repository.loadItemsStatesAndTypes()
    }

    fun loadItem(id: Int) {
        repository.loadItem(id)
    }

    fun loadItems(keyword: String = "", group1: Int = 0, group2: Int = 0) {
        repository.loadItems(keyword, group1, group2)
    }

    fun addItem(item: T, onResponse: (response: String) -> Unit) {
        repository.addItem(item, onResponse)
    }

    fun modifyItem(item: T) {
        repository.modifyItem(item)
    }

    fun modifyItemState(id: Int, state: Byte) {
        repository.modifyItemState(id, state)
    }

    fun deleteItem(id: Int) {
        repository.deleteItem(id)
    }
}