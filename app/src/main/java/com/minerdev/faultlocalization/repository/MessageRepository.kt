package com.minerdev.faultlocalization.repository

import com.minerdev.faultlocalization.base.ItemRepository
import com.minerdev.faultlocalization.model.Message
import com.minerdev.faultlocalization.retrofit.service.MessageService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class MessageRepository(service: MessageService) : ItemRepository<Message>(
    { response: String ->
        Json.decodeFromString(response)
    },
    { response: String ->
        Json.decodeFromString(response)
    },
    service
)