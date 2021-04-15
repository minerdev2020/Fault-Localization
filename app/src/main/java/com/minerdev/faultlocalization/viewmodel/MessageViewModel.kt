package com.minerdev.faultlocalization.viewmodel

import com.minerdev.faultlocalization.base.ItemViewModel
import com.minerdev.faultlocalization.model.Message
import com.minerdev.faultlocalization.repository.MessageRepository
import com.minerdev.faultlocalization.utils.Constants.ID

class MessageViewModel(repository: MessageRepository) : ItemViewModel<Message>(repository) {
    fun acceptRequest(message: Message) {
        message.reply_id = ID.toInt()
        message.state_id = 2
        modifyItem(message)
    }

    fun refuseRequest(message: Message) {
        message.reply_id = ID.toInt()
        message.state_id = 3
        modifyItem(message)
    }
}