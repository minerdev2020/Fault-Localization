package com.minerdev.faultlocalization.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Message(
    override var id: Int = 0,
    override var createdAt: String = "",
    override var updatedAt: String = "",
    override var type_id: Int = 0,
    override var state_id: Int = 0,
    override var type: String = "",
    override var state: String = "",
    var contents: String = "",
    var from_id: Int
) : Item, Parcelable