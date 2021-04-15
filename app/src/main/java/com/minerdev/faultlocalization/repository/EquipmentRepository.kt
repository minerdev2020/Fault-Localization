package com.minerdev.faultlocalization.repository

import com.minerdev.faultlocalization.base.ItemRepository
import com.minerdev.faultlocalization.model.Equipment
import com.minerdev.faultlocalization.retrofit.service.EquipmentService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class EquipmentRepository(service: EquipmentService) : ItemRepository<Equipment>(
    { response: String ->
        Json.decodeFromString(response)
    },
    { response: String ->
        Json.decodeFromString(response)
    },
    service
)