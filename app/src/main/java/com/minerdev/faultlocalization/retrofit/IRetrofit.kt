package com.minerdev.faultlocalization.retrofit

import com.minerdev.faultlocalization.model.Equipment
import com.minerdev.faultlocalization.model.Message
import com.minerdev.faultlocalization.model.Person
import com.minerdev.faultlocalization.model.Sensor
import com.minerdev.faultlocalization.utils.Constants.API_AUTH
import com.minerdev.faultlocalization.utils.Constants.API_EQUIPMENT
import com.minerdev.faultlocalization.utils.Constants.API_MESSAGE
import com.minerdev.faultlocalization.utils.Constants.API_PERSON
import com.minerdev.faultlocalization.utils.Constants.API_SENSOR
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


    @GET(API_PERSON)
    fun getAllPerson(@Header("authorization") token: String): Call<JsonObject>

    @GET("$API_PERSON/{id}")
    fun getPerson(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Call<JsonObject>

    @POST(API_PERSON)
    fun createPerson(
        @Header("authorization") token: String,
        @Body person: Person
    ): Call<JsonObject>

    @PUT("$API_PERSON/{id}")
    fun updatePerson(
        @Header("authorization") token: String,
        @Path("id") id: Int,
        @Body person: Person
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

    @DELETE(API_PERSON)
    fun deleteAllPerson(@Header("authorization") token: String): Call<JsonObject>


    @GET(API_EQUIPMENT)
    fun getAllEquipment(@Header("authorization") token: String): Call<JsonObject>

    @GET("$API_EQUIPMENT/{id}")
    fun getEquipment(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Call<JsonObject>

    @POST(API_EQUIPMENT)
    fun createEquipment(
        @Header("authorization") token: String,
        @Body equipment: Equipment
    ): Call<JsonObject>

    @PUT("$API_EQUIPMENT/{id}")
    fun updateEquipment(
        @Header("authorization") token: String,
        @Path("id") id: Int,
        @Body equipment: Equipment
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

    @DELETE(API_EQUIPMENT)
    fun deleteAllEquipment(@Header("authorization") token: String): Call<JsonObject>


    @GET(API_SENSOR)
    fun getAllSensor(@Header("authorization") token: String): Call<JsonObject>

    @GET("$API_SENSOR/{id}")
    fun getSensor(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Call<JsonObject>

    @POST(API_SENSOR)
    fun createSensor(
        @Header("authorization") token: String,
        @Body message: Sensor
    ): Call<JsonObject>

    @PUT("$API_SENSOR/{id}")
    fun updateSensor(
        @Header("authorization") token: String,
        @Path("id") id: Int,
        @Body message: Sensor
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

    @DELETE(API_SENSOR)
    fun deleteAllSensor(@Header("authorization") token: String): Call<JsonObject>


    @GET(API_MESSAGE)
    fun getAllMessage(@Header("authorization") token: String): Call<JsonObject>

    @GET("$API_MESSAGE/{id}")
    fun getMessage(
        @Header("authorization") token: String,
        @Path("id") id: Int
    ): Call<JsonObject>

    @POST(API_MESSAGE)
    fun createMessage(
        @Header("authorization") token: String,
        @Body message: Message
    ): Call<JsonObject>

    @PUT("$API_MESSAGE/{id}")
    fun updateMessage(
        @Header("authorization") token: String,
        @Path("id") id: Int,
        @Body message: Message
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

    @DELETE(API_MESSAGE)
    fun deleteAllMessage(@Header("authorization") token: String): Call<JsonObject>
}