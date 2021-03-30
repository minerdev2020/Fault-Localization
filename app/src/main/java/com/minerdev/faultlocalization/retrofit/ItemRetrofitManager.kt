package com.minerdev.faultlocalization.retrofit

import android.util.Log
import com.minerdev.faultlocalization.model.*
import com.minerdev.faultlocalization.utils.Constants
import com.minerdev.faultlocalization.utils.Constants.BASE_URL
import com.minerdev.faultlocalization.utils.Constants.TOKEN
import kotlinx.serialization.json.JsonObject
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
                "Task" -> iRetrofit?.getAllTaskStatesAndTypes(TOKEN)
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
                "Task" -> iRetrofit?.getAllTask(TOKEN, keyword.toInt(), group1, group2)
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
                "Person" -> iRetrofit?.createPerson(TOKEN, (item as Person).toJson())
                "Equipment" -> iRetrofit?.createEquipment(TOKEN, (item as Equipment).toJson())
                "Sensor" -> iRetrofit?.createSensor(TOKEN, (item as Sensor).toJson())
                "Message" -> iRetrofit?.createMessage(TOKEN, (item as Message).toJson())
                "Task" -> iRetrofit?.createTask(TOKEN, (item as Task).toJson())
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
                "Person" -> iRetrofit?.updatePerson(TOKEN, id, (item as Person).toJson())
                "Equipment" -> iRetrofit?.updateEquipment(TOKEN, id, (item as Equipment).toJson())
                "Sensor" -> iRetrofit?.updateSensor(TOKEN, id, (item as Sensor).toJson())
                "Message" -> iRetrofit?.updateMessage(TOKEN, id, (item as Message).toJson())
                "Task" -> iRetrofit?.updateTask(TOKEN, id, (item as Task).toJson())
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
                "Task" -> iRetrofit?.updateTask(TOKEN, id, state)
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
                "Task" -> iRetrofit?.deleteTask(TOKEN, id)
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
}