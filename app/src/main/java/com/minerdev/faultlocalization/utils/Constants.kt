package com.minerdev.faultlocalization.utils

import com.minerdev.faultlocalization.model.ItemState
import com.minerdev.faultlocalization.model.ItemType

object Constants {
    const val TAG = "DEBUG_TAG"

    lateinit var BASE_URL: String
    const val API_AUTH = "/api/auth"
    const val API_USER = "/api/users"
    const val API_PERSON = "/api/persons"
    const val API_EQUIPMENT = "/api/equipments"
    const val API_SENSOR = "/api/sensors"
    const val API_MESSAGE = "/api/messages"

    lateinit var USER_ID: String
    lateinit var TYPE_ID: String
    lateinit var TOKEN: String

    const val FINISH_INTERVAL_TIME = 2000

    const val FILE_MAX_SIZE: Long = 10485760

    const val CREATE = 0;
    const val UPDATE = 1;
    const val DELETE = 2;

    const val TYPE_HEADER = 0
    const val TYPE_ITEM = 1
    const val TYPE_FOOTER = 2

    val PERSON_STATE = ArrayList<ItemState>()
    val PERSON_TYPE = ArrayList<ItemType>()

    val EQUIPMENT_STATE = ArrayList<ItemState>()
    val EQUIPMENT_TYPE = ArrayList<ItemType>()

    val SENSOR_STATE = ArrayList<ItemState>()
    val SENSOR_TYPE = ArrayList<ItemType>()

    val MESSAGE_STATE = ArrayList<ItemState>()
    val MESSAGE_TYPE = ArrayList<ItemType>()
}