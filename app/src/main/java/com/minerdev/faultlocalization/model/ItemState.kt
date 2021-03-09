package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable

@Serializable
data class ItemState(
    var id: Int = 0,
    var name: String = ""
)