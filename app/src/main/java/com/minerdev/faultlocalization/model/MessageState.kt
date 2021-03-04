package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable

@Serializable
data class MessageState(
    var name: String = "",
)
