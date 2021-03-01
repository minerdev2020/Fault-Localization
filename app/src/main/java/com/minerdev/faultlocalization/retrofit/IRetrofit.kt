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

    @GET("$API_AUTH/logout")
    fun logout(@Body user: JsonElement): Call<JsonObject>

    @POST("$API_AUTH/register")
    fun register(@Body user: JsonElement): Call<JsonObject>


    @GET(API_PERSON)
    fun getAllPerson(): Call<JsonObject>

    @GET("$API_PERSON/{id}")
    fun getPerson(
        @Path("id") id: Int
    ): Call<JsonObject>

    @POST(API_PERSON)
    fun createPerson(
        @Body person: Person
    ): Call<JsonObject>

    @PUT("$API_PERSON/{id}")
    fun updatePerson(
        @Path("id") id: Int,
        @Body person: Person
    ): Call<JsonObject>

    @PATCH("$API_PERSON/{id}")
    fun updatePerson(
        @Path("id") id: Int,
        @Query("state") state: Byte
    ): Call<JsonObject>

    @DELETE("$API_PERSON/{id}")
    fun deletePerson(
        @Path("id") id: Int
    ): Call<JsonObject>

    @DELETE(API_PERSON)
    fun deleteAllPerson(): Call<JsonObject>


    @GET(API_EQUIPMENT)
    fun getAllEquipment(): Call<JsonObject>

    @GET("$API_EQUIPMENT/{id}")
    fun getEquipment(
        @Path("id") id: Int
    ): Call<JsonObject>

    @POST(API_EQUIPMENT)
    fun createEquipment(
        @Body equipment: Equipment
    ): Call<JsonObject>

    @PUT("$API_EQUIPMENT/{id}")
    fun updateEquipment(
        @Path("id") id: Int,
        @Body equipment: Equipment
    ): Call<JsonObject>

    @PATCH("$API_EQUIPMENT/{id}")
    fun updateEquipment(
        @Path("id") id: Int,
        @Query("state") state: Byte
    ): Call<JsonObject>

    @DELETE("$API_EQUIPMENT/{id}")
    fun deleteEquipment(
        @Path("id") id: Int
    ): Call<JsonObject>

    @DELETE(API_EQUIPMENT)
    fun deleteAllEquipment(): Call<JsonObject>


    @GET(API_SENSOR)
    fun getAllSensor(): Call<JsonObject>

    @GET("$API_SENSOR/{id}")
    fun getSensor(
        @Path("id") id: Int
    ): Call<JsonObject>

    @POST(API_SENSOR)
    fun createSensor(
        @Body message: Sensor
    ): Call<JsonObject>

    @PUT("$API_SENSOR/{id}")
    fun updateSensor(
        @Path("id") id: Int,
        @Body message: Sensor
    ): Call<JsonObject>

    @PATCH("$API_SENSOR/{id}")
    fun updateSensor(
        @Path("id") id: Int,
        @Query("state") state: Byte
    ): Call<JsonObject>

    @DELETE("$API_SENSOR/{id}")
    fun deleteSensor(
        @Path("id") id: Int
    ): Call<JsonObject>

    @DELETE(API_SENSOR)
    fun deleteAllSensor(): Call<JsonObject>

    
    @GET(API_MESSAGE)
    fun getAllMessage(): Call<JsonObject>

    @GET("$API_MESSAGE/{id}")
    fun getMessage(
        @Path("id") id: Int
    ): Call<JsonObject>

    @POST(API_MESSAGE)
    fun createMessage(
        @Body message: Message
    ): Call<JsonObject>

    @PUT("$API_MESSAGE/{id}")
    fun updateMessage(
        @Path("id") id: Int,
        @Body message: Message
    ): Call<JsonObject>

    @PATCH("$API_MESSAGE/{id}")
    fun updateMessage(
        @Path("id") id: Int,
        @Query("state") state: Byte
    ): Call<JsonObject>

    @DELETE("$API_MESSAGE/{id}")
    fun deleteMessage(
        @Path("id") id: Int
    ): Call<JsonObject>

    @DELETE(API_MESSAGE)
    fun deleteAllMessage(): Call<JsonObject>
}