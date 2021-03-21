package com.minerdev.faultlocalization.viewmodel

import com.minerdev.faultlocalization.base.ItemViewModel
import com.minerdev.faultlocalization.model.Message
import com.minerdev.faultlocalization.repository.MessageRepository

open class MessageViewModel(repository: MessageRepository) : ItemViewModel<Message>(repository) {
    fun acceptRequest(message: Message) {
        message.state_id = 2
        modifyItem(message)
    }

    fun refuseRequest(message: Message) {
        message.state_id = 4
        modifyItem(message)
    }
}