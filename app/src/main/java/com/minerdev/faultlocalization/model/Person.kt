package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class Person(
    override var id: Int = 0,
    override var createdAt: String = "",
    override var updatedAt: String = "",
    override var type_id: Int = 0,
    override var state_id: Int = 0,
    override var state: ItemState = ItemState(),
    override var type: ItemType = ItemType(),
    var name: String = "",
    var phone: String = "",
    var user_info: User = User(),
) : Item {
    override fun toJson() = buildJsonObject {
        put("id", id)
        put("name", name)
        put("phone", phone)
        put("type_id", type_id)
    }
}