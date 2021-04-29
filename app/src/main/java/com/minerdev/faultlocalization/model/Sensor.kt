package com.minerdev.faultlocalization.model

import com.minerdev.faultlocalization.utils.Constants.UPDATE
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class Sensor(
    override var id: Int = 0,
    override var createdAt: String = "",
    override var updatedAt: String = "",
    override var type_id: Int = 0,
    override var state_id: Int = 0,
    override var state: ItemState = ItemState(),
    override var type: ItemType = ItemType(),
    var name: String = "",
    var model_number: String = "",
    var parent_id: Int = 0,
    @Transient var editState: Int = UPDATE
) : Item {
    override fun toJson() = buildJsonObject {
        put("id", id)
        put("name", name)
        put("model_number", model_number)
        put("state_id", state_id)
        put("type_id", type_id)
        put("parent_id", parent_id)
    }
}