package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    var user_id: String = "",
    var ip: String = ""
)