package com.minerdev.faultlocalization.model

interface Item {
    var id: Int
    var createdAt: String
    var updatedAt: String
    var type: Int
    var state: Int
}
