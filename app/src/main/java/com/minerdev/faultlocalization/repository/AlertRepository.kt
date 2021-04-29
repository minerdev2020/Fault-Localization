package com.minerdev.faultlocalization.repository

import com.minerdev.faultlocalization.base.ItemRepository
import com.minerdev.faultlocalization.model.Alert
import com.minerdev.faultlocalization.retrofit.service.AlertService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class AlertRepository(service: AlertService) : ItemRepository<Alert>(
    { response: String ->
        Json.decodeFromString(response)
    },
    { response: String ->
        Json.decodeFromString(response)
    },
    service
)