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
    var state: ItemState = ItemState(),
    var type: ItemType = ItemType(),
    var sensor_info: List<Sensor> = ArrayList()
) : Item