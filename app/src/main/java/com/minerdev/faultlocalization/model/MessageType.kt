package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable

@Serializable
data class MessageType(
    var name: String = "",
)