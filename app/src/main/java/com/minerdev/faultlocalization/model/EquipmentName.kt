package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable

@Serializable
data class EquipmentName(
    var name: String = "",
    var model_number: String = "",
)
