package com.minerdev.faultlocalization.retrofit

import android.util.Log
import com.minerdev.faultlocalization.model.Equipment
import com.minerdev.faultlocalization.model.Item
import com.minerdev.faultlocalization.model.Message
import com.minerdev.faultlocalization.model.Person
import com.minerdev.faultlocalization.utils.Constants
import com.minerdev.faultlocalization.utils.Constants.BASE_URL
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitManager {
    companion object {
        val instance = RetrofitManager()
    }

    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(BASE_URL)?.create(IRetrofit::class.java)

    fun getAllItems(
        itemType: String,
        onResponse: (response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType) {
                "person" -> iRetrofit?.getAllPerson()
                "equipment" -> iRetrofit?.getAllEquipment()
                "message" -> iRetrofit?.getAllMessage()
                else -> {
                    return
                }
            }
        }

        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    onResponse(response.body().toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun getItem(
        itemType: String,
        id: Int,
        onResponse: (response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType) {
                "person" -> iRetrofit?.getPerson(id)
                "equipment" -> iRetrofit?.getEquipment(id)
                "message" -> iRetrofit?.getMessage(id)
                else -> {
                    return
                }
            }
        }

        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    onResponse(response.body().toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun <T : Item> createItem(
        itemType: String,
        item: T,
        onResponse: (response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType) {
                "person" -> iRetrofit?.createPerson(item as Person)
                "equipment" -> iRetrofit?.createEquipment(item as Equipment)
                "message" -> iRetrofit?.createMessage(item as Message)
                else -> {
                    return
                }
            }
        }

        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    onResponse(response.body().toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun updateItem(
        itemType: String,
        id: Int,
        state: Byte,
        onResponse: (response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType) {
                "person" -> iRetrofit?.updatePerson(id, state)
                "equipment" -> iRetrofit?.updateEquipment(id, state)
                "message" -> iRetrofit?.updateMessage(id, state)
                else -> {
                    return
                }
            }
        }

        Log.d(Constants.TAG, "state : $state")
        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    onResponse(response.body().toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun <T : Item> updateItem(
        itemType: String,
        id: Int,
        item: T,
        onResponse: (response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType) {
                "person" -> iRetrofit?.updatePerson(id, item as Person)
                "equipment" -> iRetrofit?.updateEquipment(id, item as Equipment)
                "message" -> iRetrofit?.updateMessage(id, item as Message)
                else -> {
                    return
                }
            }
        }

        Log.d(Constants.TAG, item.toString())
        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    onResponse(response.body().toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun deleteItem(
        itemType: String,
        id: Int,
        onResponse: (response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType) {
                "person" -> iRetrofit?.deletePerson(id)
                "equipment" -> iRetrofit?.deleteEquipment(id)
                "message" -> iRetrofit?.deleteMessage(id)
                else -> {
                    return
                }
            }
        }

        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    onResponse(response.body().toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun deleteAllItems(
        itemType: String,
        onResponse: (response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType) {
                "person" -> iRetrofit?.deleteAllPerson()
                "equipment" -> iRetrofit?.deleteAllEquipment()
                "message" -> iRetrofit?.deleteAllMessage()
                else -> {
                    return
                }
            }
        }

        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    onResponse(response.body().toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}