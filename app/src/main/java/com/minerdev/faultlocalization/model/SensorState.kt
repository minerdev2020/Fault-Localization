package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable

@Serializable
data class SensorState(
    var name: String = "",
)
