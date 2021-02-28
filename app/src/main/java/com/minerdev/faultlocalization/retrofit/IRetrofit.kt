package com.minerdev.faultlocalization.retrofit

import com.minerdev.faultlocalization.model.Equipment
import com.minerdev.faultlocalization.model.Message
import com.minerdev.faultlocalization.model.Person
import com.minerdev.faultlocalization.utils.Constants.API_AUTH
import com.minerdev.faultlocalization.utils.Constants.API_EQUIP
import com.minerdev.faultlocalization.utils.Constants.API_MESSAGE
import com.minerdev.faultlocalization.utils.Constants.API_PERSON
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {
    @GET(API_AUTH)
    fun login(): Call<JsonObject>

    @GET(API_AUTH)
    fun logout(): Call<JsonObject>

    @GET(API_AUTH)
    fun register(): Call<JsonObject>


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


    @GET(API_EQUIP)
    fun getAllEquipment(): Call<JsonObject>

    @GET("$API_EQUIP/{id}")
    fun getEquipment(
        @Path("id") id: Int
    ): Call<JsonObject>

    @Multipart
    @POST(API_EQUIP)
    fun createEquipment(
        @Body equipment: Equipment
    ): Call<JsonObject>

    @PATCH("$API_EQUIP/{id}")
    fun updateEquipment(
        @Path("id") id: Int,
        @Body equipment: Equipment
    ): Call<JsonObject>

    @PATCH("$API_EQUIP/{id}")
    fun updateEquipment(
        @Path("id") id: Int,
        @Query("state") state: Byte
    ): Call<JsonObject>

    @DELETE("$API_EQUIP/{id}")
    fun deleteEquipment(
        @Path("id") id: Int
    ): Call<JsonObject>

    @DELETE(API_EQUIP)
    fun deleteAllEquipment(): Call<JsonObject>


    @GET(API_MESSAGE)
    fun getAllMessage(): Call<JsonObject>

    @GET("$API_MESSAGE/{id}")
    fun getMessage(
        @Path("id") id: Int
    ): Call<JsonObject>

    @Multipart
    @POST(API_MESSAGE)
    fun createMessage(
        @Body message: Message
    ): Call<JsonObject>

    @PATCH("$API_MESSAGE/{id}")
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