package com.minerdev.faultlocalization.retrofit

import com.minerdev.faultlocalization.utils.Constants.API_AUTH
import com.minerdev.faultlocalization.utils.Constants.API_EQUIPMENT
import com.minerdev.faultlocalization.utils.Constants.API_MESSAGE
import com.minerdev.faultlocalization.utils.Constants.API_PERSON
import com.minerdev.faultlocalization.utils.Constants.API_SENSOR
import com.minerdev.faultlocalization.utils.Constants.API_TASK
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {
    @POST("$API_AUTH/login")
    fun login(@Body user: JsonElement): Call<JsonObject>

    @POST("$API_AUTH/logout")
    fun logout(@Header("authorization") token: String, @Body user: JsonElement): Call<JsonObject>

    @POST("$API_AUTH/register")
    fun register(@Body user: JsonElement): Call<JsonObject>

    @GET("$API_AUTH/initialize")
    fun initialize(@Header("authorization") token: String): Call<JsonObject>


    @GET("$API_PERSON/list")
    fun getAllPersonStatesAndTypes(@Header("authorization") token: String): Call<JsonObject>

    @GET(API_PERSON)
    fun getAllPerson(
        @Header("authorization") token: String,
        @Query("keyword") keyword: String = "",
        @Query("group1") group1: Int = 0,
        @Query("group2") group2: Int = 0,
    ): Call<JsonObject>

    @GET("$API_PERSON/{id}")
    fun getPerson(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Call<JsonObject>

    @POST(API_PERSON)
    fun createPerson(
        @Header("authorization") token: String,
        @Body person: JsonElement
    ): Call<JsonObject>

    @PUT("$API_PERSON/{id}")
    fun updatePerson(
        @Header("authorization") token: String,
        @Path("id") id: Int,
        @Body person: JsonElement
    ): Call<JsonObject>

    @PATCH("$API_PERSON/{id}")
    fun updatePerson(
        @Header("authorization") token: String,
        @Path("id") id: Int,
        @Query("state") state: Byte
    ): Call<JsonObject>

    @DELETE("$API_PERSON/{id}")
    fun deletePerson(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Call<JsonObject>


    @GET("$API_EQUIPMENT/list")
    fun getAllEquipmentStatesAndTypes(@Header("authorization") token: String): Call<JsonObject>

    @GET(API_EQUIPMENT)
    fun getAllEquipment(
        @Header("authorization") token: String,
        @Query("keyword") keyword: String = "",
        @Query("group1") group1: Int = 0,
        @Query("group2") group2: Int = 0,
    ): Call<JsonObject>

    @GET("$API_EQUIPMENT/{id}")
    fun getEquipment(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Call<JsonObject>

    @POST(API_EQUIPMENT)
    fun createEquipment(
        @Header("authorization") token: String,
        @Body equipment: JsonElement
    ): Call<JsonObject>

    @PUT("$API_EQUIPMENT/{id}")
    fun updateEquipment(
        @Header("authorization") token: String,
        @Path("id") id: Int,
        @Body equipment: JsonElement
    ): Call<JsonObject>

    @PATCH("$API_EQUIPMENT/{id}")
    fun updateEquipment(
        @Header("authorization") token: String,
        @Path("id") id: Int,
        @Query("state") state: Byte
    ): Call<JsonObject>

    @DELETE("$API_EQUIPMENT/{id}")
    fun deleteEquipment(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Call<JsonObject>


    @GET("$API_SENSOR/list")
    fun getAllSensorStatesAndTypes(@Header("authorization") token: String): Call<JsonObject>

    @GET(API_SENSOR)
    fun getAllSensor(
        @Header("authorization") token: String,
        @Query("keyword") keyword: String = "",
        @Query("group1") group1: Int = 0,
        @Query("group2") group2: Int = 0,
    ): Call<JsonObject>

    @GET("$API_SENSOR/{id}")
    fun getSensor(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Call<JsonObject>

    @POST(API_SENSOR)
    fun createSensor(
        @Header("authorization") token: String,
        @Body sensor: JsonElement
    ): Call<JsonObject>

    @PUT("$API_SENSOR/{id}")
    fun updateSensor(
        @Header("authorization") token: String,
        @Path("id") id: Int,
        @Body sensor: JsonElement
    ): Call<JsonObject>

    @PATCH("$API_SENSOR/{id}")
    fun updateSensor(
        @Header("authorization") token: String,
        @Path("id") id: Int,
        @Query("state") state: Byte
    ): Call<JsonObject>

    @DELETE("$API_SENSOR/{id}")
    fun deleteSensor(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Call<JsonObject>


    @GET("$API_MESSAGE/list")
    fun getAllMessageStatesAndTypes(@Header("authorization") token: String): Call<JsonObject>

    @GET(API_MESSAGE)
    fun getAllMessage(
        @Header("authorization") token: String,
        @Query("keyword") keyword: String = "",
        @Query("group1") group1: Int = 0,
        @Query("group2") group2: Int = 0,
    ): Call<JsonObject>

    @GET("$API_MESSAGE/{id}")
    fun getMessage(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Call<JsonObject>

    @POST(API_MESSAGE)
    fun createMessage(
        @Header("authorization") token: String,
        @Body message: JsonElement
    ): Call<JsonObject>

    @PUT("$API_MESSAGE/{id}")
    fun updateMessage(
        @Header("authorization") token: String,
        @Path("id") id: Int,
        @Body message: JsonElement
    ): Call<JsonObject>

    @PATCH("$API_MESSAGE/{id}")
    fun updateMessage(
        @Header("authorization") token: String,
        @Path("id") id: Int,
        @Query("state") state: Byte
    ): Call<JsonObject>

    @DELETE("$API_MESSAGE/{id}")
    fun deleteMessage(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Call<JsonObject>


    @GET("$API_TASK/list")
    fun getAllTaskStatesAndTypes(@Header("authorization") token: String): Call<JsonObject>

    @GET(API_TASK)
    fun getAllTask(
        @Header("authorization") token: String,
        @Query("person_id") person_id: Int = 0,
        @Query("group1") group1: Int = 0,
        @Query("group2") group2: Int = 0,
    ): Call<JsonObject>

    @GET("$API_TASK/{id}")
    fun getTask(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Call<JsonObject>

    @POST(API_TASK)
    fun createTask(
        @Header("authorization") token: String,
        @Body message: JsonElement
    ): Call<JsonObject>

    @PUT("$API_TASK/{id}")
    fun updateTask(
        @Header("authorization") token: String,
        @Path("id") id: Int,
        @Body message: JsonElement
    ): Call<JsonObject>

    @PATCH("$API_TASK/{id}")
    fun updateTask(
        @Header("authorization") token: String,
        @Path("id") id: Int,
        @Query("state") state: Byte
    ): Call<JsonObject>

    @DELETE("$API_TASK/{id}")
    fun deleteTask(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Call<JsonObject>
}