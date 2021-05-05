package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable

@Serializable
data class SensorData(val time: Long, val value: Float)