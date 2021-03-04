package com.minerdev.faultlocalization.repository

import com.minerdev.faultlocalization.base.Repository
import com.minerdev.faultlocalization.model.Message
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class MessageRepository : Repository<Message>(
    Message::class,
    { response: String ->
        Json.decodeFromString(response)
    },
    { response: String ->
        Json.decodeFromString(response)
    })