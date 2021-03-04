package com.minerdev.faultlocalization.viewmodel

import com.minerdev.faultlocalization.base.ItemViewModel
import com.minerdev.faultlocalization.model.Message
import com.minerdev.faultlocalization.repository.MessageRepository

open class MessageViewModel(repository: MessageRepository) : ItemViewModel<Message>(repository)