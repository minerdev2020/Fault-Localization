package com.minerdev.faultlocalization.repository

import com.minerdev.faultlocalization.base.ItemRepository
import com.minerdev.faultlocalization.model.Request
import com.minerdev.faultlocalization.retrofit.service.RequestService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class RequestRepository(service: RequestService) : ItemRepository<Request>(
    { response: String ->
        Json.decodeFromString(response)
    },
    { response: String ->
        Json.decodeFromString(response)
    },
    service
)