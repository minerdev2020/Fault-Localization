package com.minerdev.faultlocalization.viewmodel

import androidx.lifecycle.ViewModel
import com.minerdev.faultlocalization.Repository
import com.minerdev.faultlocalization.model.Item

open class ItemViewModel<T : Item>(itemType: String) : ViewModel() {
    private val repository = Repository<T>(itemType)
    val allItems = repository.allItems

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