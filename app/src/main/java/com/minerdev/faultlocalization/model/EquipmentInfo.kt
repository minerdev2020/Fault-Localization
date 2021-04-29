package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable

@Serializable
data class EquipmentInfo(
    var name: String = "",
    var model_number: String = "",
)
