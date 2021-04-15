package com.minerdev.faultlocalization.retrofit.api

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface ItemApi {
    @GET("list")
    fun getStatesAndTypes(@Header("authorization") token: String): Call<JsonObject>

    @GET(".")
    fun getAll(
        @Header("authorization") token: String,
        @Query("keyword") keyword: String,
        @Query("group1") group1: Int,
        @Query("group2") group2: Int,
    ): Call<JsonObject>

    @GET("{id}")
    fun get(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Call<JsonObject>

    @POST(".")
    fun create(
        @Header("authorization") token: String,
        @Body equipment: JsonElement
    ): Call<JsonObject>

    @PUT("{id}")
    fun update(
        @Header("authorization") token: String,
        @Path("id") id: Int,
        @Body equipment: JsonElement
    ): Call<JsonObject>

    @PATCH("{id}")
    fun update(
        @Header("authorization") token: String,
        @Path("id") id: Int,
        @Query("state") state: Byte
    ): Call<JsonObject>

    @DELETE("{id}")
    fun delete(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Call<JsonObject>
}