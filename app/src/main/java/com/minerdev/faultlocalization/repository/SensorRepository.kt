package com.minerdev.faultlocalization.repository

import android.content.Context
import com.minerdev.faultlocalization.base.Repository
import com.minerdev.faultlocalization.model.Sensor
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class SensorRepository(context: Context) : Repository<Sensor>(
    context,
    Sensor::class,
    { response: String ->
        Json.decodeFromString(response)
    },
    { response: String ->
        Json.decodeFromString(response)
    })