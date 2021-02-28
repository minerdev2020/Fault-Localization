package com.minerdev.faultlocalization.retrofit

import com.minerdev.faultlocalization.model.Person
import com.minerdev.faultlocalization.utils.Constants.BASE_URL
import kotlinx.serialization.json.JsonObject
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
        val call = iRetrofit?.getAllPerson() ?: return
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
        id: Int,
        onResponse: (response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = iRetrofit?.getPerson(id) ?: return
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
        person: Person,
        onResponse: (response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val call = iRetrofit?.createPerson(person) ?: return
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