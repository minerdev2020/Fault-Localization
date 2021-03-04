package com.minerdev.faultlocalization.repository

import com.minerdev.faultlocalization.base.Repository
import com.minerdev.faultlocalization.model.Equipment
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class EquipmentRepository : Repository<Equipment>(
    Equipment::class,
    { response: String ->
        Json.decodeFromString(response)
    },
    { response: String ->
        Json.decodeFromString(response)
    })