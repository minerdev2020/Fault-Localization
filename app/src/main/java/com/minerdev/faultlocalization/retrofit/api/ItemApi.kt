package com.minerdev.faultlocalization.retrofit.api

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface ItemApi {
    @GET("list")
    fun getStatesAndTypes(@Header("authorization") token: String): Call<JsonObject>

    @GET(".")
    fun getAllItems(
        @Header("authorization") token: String,
        @Query("keyword") keyword: String,
        @Query("group1") group1: Int,
        @Query("group2") group2: Int,
    ): Call<JsonObject>

    @GET("{id}")
    fun getItem(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Call<JsonObject>

    @POST(".")
    fun createItem(
        @Header("authorization") token: String,
        @Body equipment: JsonElement
    ): Call<JsonObject>

    @PUT("{id}")
    fun updateItem(
        @Header("authorization") token: String,
        @Path("id") id: Int,
        @Body equipment: JsonElement
    ): Call<JsonObject>

    @PATCH("{id}")
    fun updateItem(
        @Header("authorization") token: String,
        @Path("id") id: Int,
        @Query("state") state: Byte
    ): Call<JsonObject>

    @DELETE("{id}")
    fun deleteItem(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Call<JsonObject>
}