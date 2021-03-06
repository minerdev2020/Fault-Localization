package com.minerdev.faultlocalization.model

import com.minerdev.faultlocalization.utils.Constants.UPDATE
import kotlinx.serialization.Serializable

@Serializable
data class Sensor(
    override var id: Int = 0,
    override var createdAt: String = "",
    override var updatedAt: String = "",
    override var type_id: Int = 0,
    override var state_id: Int = 0,
    var name: String = "",
    var model_number: String = "",
    var parent_id: Int = 0,
    var SensorState: SensorState,
    var SensorType: SensorType,
    var state: Int = UPDATE
) : Item