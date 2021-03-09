package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable

@Serializable
data class ItemType(
    var id: Int = 0,
    var name: String = ""
)