package com.minerdev.faultlocalization.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginLog(
    var id: Int = 0,
    var date: String = "",
    var user_id: String = "",
    var ip: String = ""
)