package com.minerdev.faultlocalization.repository

import com.minerdev.faultlocalization.base.ItemRepository
import com.minerdev.faultlocalization.model.Sensor
import com.minerdev.faultlocalization.retrofit.service.SensorService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class SensorRepository(service: SensorService) : ItemRepository<Sensor>(
    { response: String ->
        Json.decodeFromString(response)
    },
    { response: String ->
        Json.decodeFromString(response)
    },
    service
)