package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonState(
    var name: String = "",
)