package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable

@Serializable
data class SensorType(
    var name: String = "",
)