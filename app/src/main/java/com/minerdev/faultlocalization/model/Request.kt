package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class Request(
    override var id: Int = 0,
    override var createdAt: String = "",
    override var updatedAt: String = "",
    override var type_id: Int = 0,
    override var state_id: Int = 0,
    override var state: ItemState = ItemState(),
    override var type: ItemType = ItemType(),
    var estimated_time: Float = 0f,
    var contents: String = "",
    var equipment_id: Int = 0,
    var from_id: Int = 0,
    var reply_id: Int? = null,
    var from: PersonInfo = PersonInfo(),
    var replyer: PersonInfo? = null,
    var equipment_info: EquipmentInfo = EquipmentInfo()
) : Item {
    override fun toJson() = buildJsonObject {
        put("id", id)
        put("estimated_time", estimated_time)
        put("contents", contents)
        put("state_id", state_id)
        put("type_id", type_id)
        put("from_id", from_id)
        put("reply_id", reply_id)
        put("equipment_id", equipment_id)
    }
}