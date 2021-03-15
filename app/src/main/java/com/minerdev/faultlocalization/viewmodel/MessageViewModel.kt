package com.minerdev.faultlocalization.viewmodel

import com.minerdev.faultlocalization.base.ItemViewModel
import com.minerdev.faultlocalization.model.Message
import com.minerdev.faultlocalization.repository.MessageRepository
import com.minerdev.faultlocalization.utils.Constants

open class MessageViewModel(repository: MessageRepository) : ItemViewModel<Message>(repository) {
    fun addAcceptMessage(id: Int) {
        addItem(
            Message(
                type_id = 1,
                state_id = 1,
                contents = "contents",
                from_id = Constants.ID.toInt()
            )
        ) {}
    }

    fun addRefuseMessage(id: Int) {
        addItem(
            Message(
                type_id = 1,
                state_id = 1,
                contents = "contents",
                from_id = Constants.ID.toInt()
            )
        ) {}
    }
}