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
    var MessageState: MessageState,
    var MessageType: MessageType,
    var From: From
) : Item