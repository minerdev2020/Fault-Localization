package com.minerdev.faultlocalization.retrofit.api

import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface LoginLogApi {
    @GET("{id}")
    fun getLoginLogs(@Path("id") id: Int): Call<JsonObject>
}