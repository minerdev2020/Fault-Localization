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
    override var type: Int = 0,
    override var state: Int = 0,
    var fromWho: String = "",
    var contents: String = ""
) : Item, Parcelable