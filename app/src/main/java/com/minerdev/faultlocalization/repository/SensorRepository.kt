package com.minerdev.faultlocalization.repository

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.minerdev.faultlocalization.base.Repository
import com.minerdev.faultlocalization.model.Sensor
import com.minerdev.faultlocalization.view.activity.SplashActivity
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
    },
    {
        val intent = Intent(context, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        ContextCompat.startActivity(context, intent, null)
    }
)