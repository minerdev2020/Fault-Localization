package com.minerdev.faultlocalization.utils

object Constants {
    const val TAG = "DEBUG_TAG"

    lateinit var BASE_URL: String
    const val API_AUTH = "/api/auth"
    const val API_USER = "/api/users"
    const val API_PERSON = "/api/persons"
    const val API_EQUIPMENT = "/api/equipments"
    const val API_SENSOR = "/api/sensors"
    const val API_MESSAGE = "/api/messages"

    const val FINISH_INTERVAL_TIME = 2000

    const val FILE_MAX_SIZE: Long = 10485760

    const val CREATE = 0;
    const val UPDATE = 1;
    const val DELETE = 2;
}