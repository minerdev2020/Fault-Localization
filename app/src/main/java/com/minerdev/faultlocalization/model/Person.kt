package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable

@Serializable
data class Person(
    override var id: Int = 0,
    override var createdAt: String = "",
    override var updatedAt: String = "",
    override var type_id: Int = 0,
    override var state_id: Int = 0,
    var name: String = "",
    var phone: String = "",
    var user_info: User = User(),
    var state: ItemState = ItemState(),
    var type: ItemType = ItemType()
) : Item