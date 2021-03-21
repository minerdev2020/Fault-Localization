package com.minerdev.faultlocalization.retrofit

import android.util.Log
import com.minerdev.faultlocalization.model.*
import com.minerdev.faultlocalization.utils.Constants
import com.minerdev.faultlocalization.utils.Constants.BASE_URL
import com.minerdev.faultlocalization.utils.Constants.TOKEN
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.reflect.KClass

class ItemRetrofitManager {
    companion object {
        val instance = ItemRetrofitManager()
    }

    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(BASE_URL)?.create(IRetrofit::class.java)

    fun getAllItemsStatesAndTypes(
        itemType: KClass<*>,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType.simpleName) {
                "Person" -> iRetrofit?.getAllPersonStatesAndTypes(TOKEN)
                "Equipment" -> iRetrofit?.getAllEquipmentStatesAndTypes(TOKEN)
                "Sensor" -> iRetrofit?.getAllSensorStatesAndTypes(TOKEN)
                "Message" -> iRetrofit?.getAllMessageStatesAndTypes(TOKEN)
                else -> {
                    return
                }
            }
        }

        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onAcceptance(response.code(), it.toString())
                    }

                } else {
                    response.errorBody()?.let {
                        onRejection(response.code(), it.string())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun getAllItems(
        itemType: KClass<*>,
        keyword: String = "",
        group1: Int = 0,
        group2: Int = 0,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType.simpleName) {
                "Person" -> iRetrofit?.getAllPerson(TOKEN, keyword, group1, group2)
                "Equipment" -> iRetrofit?.getAllEquipment(TOKEN, keyword, group1, group2)
                "Message" -> iRetrofit?.getAllMessage(TOKEN, keyword, group1, group2)
                else -> {
                    return
                }
            }
        }

        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onAcceptance(response.code(), it.toString())
                    }

                } else {
                    response.errorBody()?.let {
                        onRejection(response.code(), it.string())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun getItem(
        itemType: KClass<*>,
        id: Int,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType.simpleName) {
                "Person" -> iRetrofit?.getPerson(TOKEN, id)
                "Equipment" -> iRetrofit?.getEquipment(TOKEN, id)
                "Message" -> iRetrofit?.getMessage(TOKEN, id)
                else -> {
                    return
                }
            }
        }

        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onAcceptance(response.code(), it.toString())
                    }

                } else {
                    response.errorBody()?.let {
                        onRejection(response.code(), it.string())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun <T : Item> createItem(
        itemType: KClass<*>,
        item: T,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType.simpleName) {
                "Person" -> iRetrofit?.createPerson(TOKEN, personToJsonElement(item as Person))
                "Equipment" -> iRetrofit?.createEquipment(
                    TOKEN,
                    equipmentToJsonElement(item as Equipment)
                )
                "Sensor" -> iRetrofit?.createSensor(
                    TOKEN,
                    sensorToJsonElement(item as Sensor)
                )
                "Message" -> iRetrofit?.createMessage(TOKEN, messageToJsonElement(item as Message))
                else -> {
                    return
                }
            }
        }

        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onAcceptance(response.code(), it.toString())
                    }

                } else {
                    response.errorBody()?.let {
                        onRejection(response.code(), it.string())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun <T : Item> updateItem(
        itemType: KClass<*>,
        id: Int,
        item: T,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType.simpleName) {
                "Person" -> iRetrofit?.updatePerson(TOKEN, id, personToJsonElement(item as Person))
                "Equipment" -> iRetrofit?.updateEquipment(
                    TOKEN,
                    id,
                    equipmentToJsonElement(item as Equipment)
                )
                "Sensor" -> iRetrofit?.updateSensor(
                    TOKEN,
                    id,
                    sensorToJsonElement(item as Sensor)
                )
                "Message" -> iRetrofit?.updateMessage(
                    TOKEN,
                    id,
                    messageToJsonElement(item as Message)
                )
                else -> {
                    return
                }
            }
        }

        Log.d(Constants.TAG, item.toString())
        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onAcceptance(response.code(), it.toString())
                    }

                } else {
                    response.errorBody()?.let {
                        onRejection(response.code(), it.string())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun updateItem(
        itemType: KClass<*>,
        id: Int,
        state: Byte,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType.simpleName) {
                "Person" -> iRetrofit?.updatePerson(TOKEN, id, state)
                "Equipment" -> iRetrofit?.updateEquipment(TOKEN, id, state)
                "Sensor" -> iRetrofit?.updateSensor(TOKEN, id, state)
                "Message" -> iRetrofit?.updateMessage(TOKEN, id, state)
                else -> {
                    return
                }
            }
        }

        Log.d(Constants.TAG, "state : $state")
        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onAcceptance(response.code(), it.toString())
                    }

                } else {
                    response.errorBody()?.let {
                        onRejection(response.code(), it.string())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun deleteItem(
        itemType: KClass<*>,
        id: Int,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType.simpleName) {
                "Person" -> iRetrofit?.deletePerson(TOKEN, id)
                "Equipment" -> iRetrofit?.deleteEquipment(TOKEN, id)
                "Sensor" -> iRetrofit?.deleteSensor(TOKEN, id)
                "Message" -> iRetrofit?.deleteMessage(TOKEN, id)
                else -> {
                    return
                }
            }
        }

        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onAcceptance(response.code(), it.toString())
                    }

                } else {
                    response.errorBody()?.let {
                        onRejection(response.code(), it.string())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    private fun personToJsonElement(person: Person) = buildJsonObject {
        put("id", person.id)
        put("name", person.name)
        put("phone", person.phone)
        put("type_id", person.type_id)
    }

    private fun equipmentToJsonElement(equipment: Equipment) = buildJsonObject {
        put("id", equipment.id)
        put("name", equipment.name)
        put("model_number", equipment.model_number)
        put("state_id", equipment.state_id)
        put("type_id", equipment.type_id)
    }

    private fun sensorToJsonElement(sensor: Sensor) = buildJsonObject {
        put("id", sensor.id)
        put("name", sensor.name)
        put("model_number", sensor.model_number)
        put("state_id", sensor.state_id)
        put("type_id", sensor.type_id)
        put("parent_id", sensor.parent_id)
    }

    private fun messageToJsonElement(message: Message) = buildJsonObject {
        put("id", message.id)
        put("contents", message.contents)
        put("state_id", message.state_id)
        put("type_id", message.type_id)
        put("from_id", message.from_id)
    }
}