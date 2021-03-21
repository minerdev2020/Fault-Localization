package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    override var id: Int = 0,
    override var createdAt: String = "",
    override var updatedAt: String = "",
    override var type_id: Int = 0,
    override var state_id: Int = 0,
    var contents: String = "",
    var from_id: Int = 0,
    var reply_id: Int? = null,
    var state: ItemState = ItemState(),
    var type: ItemType = ItemType(),
    var from: PersonName = PersonName(),
    var replyer: PersonName? = null
) : Item