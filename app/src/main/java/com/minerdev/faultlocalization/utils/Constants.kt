package com.minerdev.greformanager.utils

import java.util.*

object Constants {
    const val TAG = "DEBUG_TAG"

    lateinit var BASE_URL: String
    const val API_HOUSE = "/api/houses"
    const val API_IMAGE = "/api/images"
    lateinit var API_DAUM_ADDRESS: String
    const val API_NAVER_MAP = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode"

    lateinit var NAVER_CLIENT_ID: String
    lateinit var NAVER_CLIENT_SECRET: String
    const val NAVER_API_KEY_ID_HEADER = "X-NCP-APIGW-API-KEY-ID"
    const val NAVER_API_KEY_HEADER = "X-NCP-APIGW-API-KEY"

    const val FINISH_INTERVAL_TIME = 2000

    const val FILE_MAX_SIZE: Long = 10485760

    const val SALE = 0
    const val SOLD = 1

    const val PYEONG_TO_METER = 3.305f
    const val METER_TO_PYEONG = 0.3025f

    const val HOUSE_DETAIL_ACTIVITY_REQUEST_CODE = 1
    const val HOUSE_MODIFY_ACTIVITY_REQUEST_CODE = 2

    const val CREATE = 0;
    const val UPDATE = 1;
    const val DELETE = 2;

    val PAYMENT_TYPE = ArrayList<ArrayList<String>>()
    val HOUSE_TYPE = ArrayList<String>()
    val STRUCTURE = ArrayList<String>()
    val DIRECTION = ArrayList<String>()
    val BATHROOM = ArrayList<String>()

    private var isInitialized = false
}