package com.minerdev.faultlocalization.utils

import android.app.Application

object Constants {
    lateinit var APPLICATION: Application

    const val TAG = "DEBUG_TAG"

    lateinit var BASE_URL: String
    const val API_AUTH = "/api/auth"
    const val API_PERSON = "/api/persons"
    const val API_EQUIPMENT = "/api/equipments"
    const val API_SENSOR = "/api/sensors"
    const val API_MESSAGE = "/api/messages"
    const val API_TASK = "/api/tasks"
    const val API_DATA = "/api/data"

    lateinit var ID: String
    lateinit var USER_ID: String
    lateinit var TYPE_ID: String
    lateinit var TOKEN: String

    const val FINISH_INTERVAL_TIME = 2000

    const val FILE_MAX_SIZE: Long = 10485760

    const val CREATE = 0
    const val UPDATE = 1
    const val DELETE = 2

    const val TYPE_HEADER = 0
    const val TYPE_ITEM = 1
    const val TYPE_FOOTER = 2
}