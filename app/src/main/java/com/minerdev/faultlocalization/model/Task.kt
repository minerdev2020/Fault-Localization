package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class Task(
    override var id: Int = 0,
    override var createdAt: String = "",
    override var updatedAt: String = "",
    override var type_id: Int = 0,
    override var state_id: Int = 0,
    var state: ItemState = ItemState(),
    var type: ItemType = ItemType(),
    var repairman_id: Int = 0,
    var target_id: Int = 0,
    var repairman: Person = Person(),
    var target: Equipment = Equipment()
) : Item {
    override fun toJson() = buildJsonObject {
        put("id", id)
        put("state_id", state_id)
        put("type_id", type_id)
        put("repairman_id", repairman.id)
        put("target_id", target.id)
    }
}