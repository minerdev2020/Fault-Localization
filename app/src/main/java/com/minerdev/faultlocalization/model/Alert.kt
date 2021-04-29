package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class Alert(
    override var id: Int = 0,
    override var createdAt: String = "",
    override var updatedAt: String = "",
    override var type_id: Int = 0,
    override var state_id: Int = 0,
    override var state: ItemState = ItemState(),
    override var type: ItemType = ItemType(),
    var breakdown_id: Int = 0,
    var breakdown_cause: String = "",
    var breakdown_info: EquipmentInfo = EquipmentInfo()
) : Item {
    override fun toJson() = buildJsonObject {
        put("id", id)
        put("state_id", state_id)
        put("type_id", type_id)
        put("breakdown_id", breakdown_id)
        put("breakdown_cause", breakdown_cause)
    }
}