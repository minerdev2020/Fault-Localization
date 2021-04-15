package com.minerdev.faultlocalization.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.minerdev.faultlocalization.utils.Constants.BASE_URL
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitClient {
    fun getClient(apiUrl: String): Retrofit? {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL + apiUrl)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }
}