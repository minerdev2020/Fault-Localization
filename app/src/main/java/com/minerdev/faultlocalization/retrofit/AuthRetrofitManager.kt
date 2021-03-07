package com.minerdev.faultlocalization.retrofit

import com.minerdev.faultlocalization.utils.Constants.BASE_URL
import com.minerdev.faultlocalization.utils.Constants.TOKEN
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRetrofitManager {
    companion object {
        val instance = AuthRetrofitManager()
    }

    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(BASE_URL)?.create(IRetrofit::class.java)

    fun login(
        id: String,
        pw: String,
        onResponse: (response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val user = buildJsonObject {
            put("user_id", id)
            put("user_pw", pw)
        }
        val call = iRetrofit?.login(user) ?: return
        call.enqueue(object : Callback<JsonObject> {
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

    fun logout(
        id: String,
        onResponse: (response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val user = buildJsonObject {
            put("user_id", id)
        }
        val call = iRetrofit?.logout(TOKEN, user) ?: return
        call.enqueue(object : Callback<JsonObject> {
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

    fun register(
        id: String,
        pw: String,
        name: String,
        phone: String,
        typeId: Int,
        onResponse: (response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val user = buildJsonObject {
            put("user_id", id)
            put("user_pw", pw)
            put("name", name)
            put("phone", phone)
            put("type_id", typeId)
        }
        val call = iRetrofit?.register(user) ?: return
        call.enqueue(object : Callback<JsonObject> {
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

    fun initialize(
        onResponse: (response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = iRetrofit?.initialize(TOKEN) ?: return
        call.enqueue(object : Callback<JsonObject> {
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