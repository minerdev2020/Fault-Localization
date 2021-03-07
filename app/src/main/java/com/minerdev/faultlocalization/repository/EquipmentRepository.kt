package com.minerdev.faultlocalization.repository

import android.content.Context
import com.minerdev.faultlocalization.base.Repository
import com.minerdev.faultlocalization.model.Equipment
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class EquipmentRepository(context: Context) : Repository<Equipment>(
    context,
    Equipment::class,
    { response: String ->
        Json.decodeFromString(response)
    },
    { response: String ->
        Json.decodeFromString(response)
    })