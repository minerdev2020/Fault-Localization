package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonName(
    var name: String = "",
)
