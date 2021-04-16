package com.minerdev.faultlocalization.retrofit.service

import android.util.Log
import com.minerdev.faultlocalization.model.Item
import com.minerdev.faultlocalization.retrofit.RetrofitClient
import com.minerdev.faultlocalization.retrofit.api.ItemApi
import com.minerdev.faultlocalization.utils.Constants
import com.minerdev.faultlocalization.utils.Constants.TOKEN
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class ItemServiceImpl<T : Item>(apiUrl: String) : ItemService {
    private val client = RetrofitClient.getClient(apiUrl)?.create(ItemApi::class.java)

    override fun getStatesAndTypes(
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = client?.getStatesAndTypes(TOKEN) ?: return
        call.enqueue(object : Callback<JsonObject> {
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

    override fun getAllItems(
        keyword: String,
        group1: Int,
        group2: Int,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = client?.getAllItems(TOKEN, keyword, group1, group2) ?: return
        call.enqueue(object : Callback<JsonObject> {
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

    override fun getItem(
        id: Int,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = client?.getItem(TOKEN, id) ?: return
        call.enqueue(object : Callback<JsonObject> {
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

    override fun createItem(
        item: Item,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = client?.createItem(TOKEN, (item as T).toJson()) ?: return
        call.enqueue(object : Callback<JsonObject> {
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

    override fun updateItem(
        id: Int,
        item: Item,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = client?.updateItem(TOKEN, id, (item as T).toJson()) ?: return
        Log.d(Constants.TAG, item.toString())
        call.enqueue(object : Callback<JsonObject> {
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

    override fun updateItem(
        id: Int,
        state: Byte,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = client?.updateItem(TOKEN, id, state) ?: return
        Log.d(Constants.TAG, "state : $state")
        call.enqueue(object : Callback<JsonObject> {
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

    override fun deleteItem(
        id: Int,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = client?.deleteItem(TOKEN, id) ?: return
        call.enqueue(object : Callback<JsonObject> {
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