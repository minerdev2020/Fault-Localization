package com.minerdev.faultlocalization.model

interface Item {
    var id: Int
    var createdAt: String
    var updatedAt: String
    var type_id: Int
    var state_id: Int
    var type: String
    var state: String
}
