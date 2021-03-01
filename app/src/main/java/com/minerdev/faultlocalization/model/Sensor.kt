package com.minerdev.faultlocalization.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Sensor(
    override var id: Int = 0,
    override var createdAt: String = "",
    override var updatedAt: String = "",
    override var type_id: Int = 0,
    override var state_id: Int = 0,
    override var type: String = "",
    override var state: String = "",
    var name: String = "",
    var number: String = "",
    var parent_id: Int = 0,
) : Item, Parcelable