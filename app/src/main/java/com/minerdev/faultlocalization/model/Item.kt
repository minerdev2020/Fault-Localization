package com.minerdev.faultlocalization.model

import kotlinx.serialization.json.JsonObject

interface Item {
    var id: Int
    var createdAt: String
    var updatedAt: String
    var type_id: Int
    var state_id: Int

    fun toJson(): JsonObject
}
