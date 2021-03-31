package com.minerdev.faultlocalization.base

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.minerdev.faultlocalization.model.Item
import com.minerdev.faultlocalization.model.ItemState
import com.minerdev.faultlocalization.model.ItemType
import com.minerdev.faultlocalization.retrofit.ItemRetrofitManager
import com.minerdev.faultlocalization.utils.AppHelper
import com.minerdev.faultlocalization.utils.Constants.TAG
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import kotlin.reflect.KClass

open class Repository<T : Item>(
    private val context: Context,
    private val itemType: KClass<T>,
    private val jsonToItemConverter: (response: String) -> T,
    private val jsonToItemListConverter: (response: String) -> List<T>,
    private val onInvalidToken: () -> Unit = {}
) {
    val allItems = MutableLiveData<List<T>>()
    val item = MutableLiveData<T>()

    val itemStates = MutableLiveData<List<ItemState>>()
    val itemTypes = MutableLiveData<List<ItemType>>()

    fun loadItemStatesAndTypes() {
        ItemRetrofitManager.instance.getAllItemsStatesAndTypes(itemType,
            { _: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "loadItemStatesAndTypes response : " + jsonResponse.getString("message"))
                Log.d(TAG, "loadItemStatesAndTypes response : " + jsonResponse.getString("data"))

                val statesAndTypes = JSONObject(jsonResponse.getString("data"))
                itemStates.postValue(Json.decodeFromString(statesAndTypes.getString("states")))
                itemTypes.postValue(Json.decodeFromString(statesAndTypes.getString("types")))
            },
            { code: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "loadItemStatesAndTypes response : " + jsonResponse.getString("message"))
                checkTokenResponse(code)
            },
            { error: Throwable ->
                Log.d(TAG, "loadItems error : " + error.localizedMessage)
            })
    }

    fun loadItem(id: Int) {
        ItemRetrofitManager.instance.getItem(itemType, id,
            { _: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "loadItem response : " + jsonResponse.getString("message"))
                Log.d(TAG, "loadItem response : " + jsonResponse.getString("data"))
                item.postValue(jsonToItemConverter(jsonResponse.getString("data")))
            },
            { code: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "loadItem response : " + jsonResponse.getString("message"))
                checkTokenResponse(code)
            },
            { error: Throwable ->
                Log.d(TAG, "loadItem error : " + error.localizedMessage)
            })
    }

    fun loadItems(keyword: String = "", group1: Int = 0, group2: Int = 0) {
        ItemRetrofitManager.instance.getAllItems(itemType, keyword, group1, group2,
            { _: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "loadItems response : " + jsonResponse.getString("message"))
//                Log.d(TAG, "loadItems response : " + jsonResponse.getString("data"))
                allItems.postValue(jsonToItemListConverter(jsonResponse.getString("data")))
            },
            { code: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "loadItems response : " + jsonResponse.getString("message"))
                checkTokenResponse(code)
            },
            { error: Throwable ->
                Log.d(TAG, "loadItems error : " + error.localizedMessage)
            })
    }

    fun addItem(item: T, onResponse: (response: String?) -> Unit = {}) {
        ItemRetrofitManager.instance.createItem(itemType, item,
            { _: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "addItem response : " + jsonResponse.getString("message"))
                Log.d(TAG, "addItem response : " + jsonResponse.getString("data"))
                onResponse(jsonResponse.getString("data"))
            },
            { code: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "addItem response : " + jsonResponse.getString("message"))
                checkTokenResponse(code)
            },
            { error: Throwable ->
                Log.d(TAG, "addItem error : " + error.localizedMessage)
            })
    }

    fun modifyItem(item: T, onResponse: (response: String?) -> Unit = {}) {
        ItemRetrofitManager.instance.updateItem(itemType, item.id, item,
            { _: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "modifyItem response : " + jsonResponse.getString("message"))
                onResponse(null)
            },
            { code: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "modifyItem response : " + jsonResponse.getString("message"))
                checkTokenResponse(code)
            },
            { error: Throwable ->
                Log.d(TAG, "modifyItem error : " + error.localizedMessage)
            })
    }

    fun modifyItemState(id: Int, state: Byte, onResponse: (response: String?) -> Unit = {}) {
        ItemRetrofitManager.instance.updateItem(itemType, id, state,
            { _: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "modifyItemState response : " + jsonResponse.getString("message"))
                onResponse(null)
            },
            { code: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "modifyItemState response : " + jsonResponse.getString("message"))
                checkTokenResponse(code)
            },
            { error: Throwable ->
                Log.d(TAG, "modifyItemState error : " + error.localizedMessage)
            })
    }

    fun deleteItem(id: Int, onResponse: (response: String?) -> Unit = {}) {
        ItemRetrofitManager.instance.deleteItem(itemType, id,
            { _: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "deleteItem response : " + jsonResponse.getString("message"))
                onResponse(null)
            },
            { code: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "deleteItem response : " + jsonResponse.getString("message"))
                checkTokenResponse(code)
            },
            { error: Throwable ->
                Log.d(TAG, "deleteItem error : " + error.localizedMessage)
            })
    }

    private fun checkTokenResponse(code: Int) {
        when (code) {
            401 -> {
                Log.d(TAG, "无效的Token！")
                Toast.makeText(context, "无效的Token！请重新登录！", Toast.LENGTH_SHORT).show()
                AppHelper.logout(onInvalidToken, onInvalidToken)
            }
            419 -> {
                Log.d(TAG, "该Token已过期！请重新登录！")
                Toast.makeText(context, "该Token已过期！请重新登录！", Toast.LENGTH_SHORT).show()
                AppHelper.logout(onInvalidToken, onInvalidToken)
            }
            else -> {
            }
        }
    }
}