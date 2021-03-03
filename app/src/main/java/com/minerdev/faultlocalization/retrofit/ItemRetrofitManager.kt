package com.minerdev.faultlocalization.retrofit

import android.util.Log
import com.minerdev.faultlocalization.model.Equipment
import com.minerdev.faultlocalization.model.Item
import com.minerdev.faultlocalization.model.Message
import com.minerdev.faultlocalization.model.Person
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

    fun getAllItems(
        itemType: KClass<*>,
        onResponse: (response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType.simpleName) {
                "Person" -> iRetrofit?.getAllPerson(TOKEN)
                "Equipment" -> iRetrofit?.getAllEquipment(TOKEN)
                "Message" -> iRetrofit?.getAllMessage(TOKEN)
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
        itemType: KClass<*>,
        id: Int,
        onResponse: (response: String) -> Unit,
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
        itemType: KClass<*>,
        item: T,
        onResponse: (response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType.simpleName) {
                "Person" -> iRetrofit?.createPerson(TOKEN, item as Person)
                "Equipment" -> iRetrofit?.createEquipment(TOKEN, item as Equipment)
                "Message" -> iRetrofit?.createMessage(TOKEN, item as Message)
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
        itemType: KClass<*>,
        id: Int,
        state: Byte,
        onResponse: (response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType.simpleName) {
                "Person" -> iRetrofit?.updatePerson(TOKEN, id, state)
                "Equipment" -> iRetrofit?.updateEquipment(TOKEN, id, state)
                "Message" -> iRetrofit?.updateMessage(TOKEN, id, state)
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
        itemType: KClass<*>,
        id: Int,
        item: T,
        onResponse: (response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType.simpleName) {
                "Person" -> iRetrofit?.updatePerson(TOKEN, id, item as Person)
                "Equipment" -> iRetrofit?.updateEquipment(TOKEN, id, item as Equipment)
                "Message" -> iRetrofit?.updateMessage(TOKEN, id, item as Message)
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
        itemType: KClass<*>,
        id: Int,
        onResponse: (response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType.simpleName) {
                "Person" -> iRetrofit?.deletePerson(TOKEN, id)
                "Equipment" -> iRetrofit?.deleteEquipment(TOKEN, id)
                "Message" -> iRetrofit?.deleteMessage(TOKEN, id)
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
        itemType: KClass<*>,
        onResponse: (response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = run {
            when (itemType.simpleName) {
                "Person" -> iRetrofit?.deleteAllPerson(TOKEN)
                "Equipment" -> iRetrofit?.deleteAllEquipment(TOKEN)
                "Message" -> iRetrofit?.deleteAllMessage(TOKEN)
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