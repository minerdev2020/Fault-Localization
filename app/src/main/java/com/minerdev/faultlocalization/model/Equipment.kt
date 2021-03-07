package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable

@Serializable
data class Equipment(
    override var id: Int = 0,
    override var createdAt: String = "",
    override var updatedAt: String = "",
    override var type_id: Int = 0,
    override var state_id: Int = 0,
    var name: String = "",
    var model_number: String = "",
    var EquipmentState: EquipmentState,
    var EquipmentType: EquipmentType,
    var Sensors: List<Sensor>
) : Item