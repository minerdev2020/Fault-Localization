package com.minerdev.faultlocalization.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.minerdev.faultlocalization.model.Item
import com.minerdev.faultlocalization.model.ItemState
import com.minerdev.faultlocalization.model.ItemType
import com.minerdev.faultlocalization.retrofit.service.ItemService
import com.minerdev.faultlocalization.utils.AppHelper
import com.minerdev.faultlocalization.utils.Constants.TAG
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject

open class ItemRepository<T : Item>(
    private val jsonToItemConverter: (response: String) -> T,
    private val jsonToItemListConverter: (response: String) -> List<T>,
    private val service: ItemService
) {
    val allItems = MutableLiveData<List<T>>()
    val item = MutableLiveData<T>()

    val itemStates = MutableLiveData<List<ItemState>>()
    val itemTypes = MutableLiveData<List<ItemType>>()

    fun loadItemStatesAndTypes() {
        service.getStatesAndTypes(
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
                AppHelper.checkTokenResponse(code)
            },
            { error: Throwable ->
                Log.d(TAG, "loadItems error : " + error.localizedMessage)
            })
    }

    fun loadItem(id: Int) {
        service.getItem(id,
            { _: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "loadItem response : " + jsonResponse.getString("message"))
                Log.d(TAG, "loadItem response : " + jsonResponse.getString("data"))
                item.postValue(jsonToItemConverter(jsonResponse.getString("data")))
            },
            { code: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "loadItem response : " + jsonResponse.getString("message"))
                AppHelper.checkTokenResponse(code)
            },
            { error: Throwable ->
                Log.d(TAG, "loadItem error : " + error.localizedMessage)
            })
    }

    fun loadItems(keyword: String = "", group1: Int = 0, group2: Int = 0) {
        service.getAllItems(keyword, group1, group2,
            { _: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "loadItems response : " + jsonResponse.getString("message"))
//                Log.d(TAG, "loadItems response : " + jsonResponse.getString("data"))
                allItems.postValue(jsonToItemListConverter(jsonResponse.getString("data")))
            },
            { code: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "loadItems response : " + jsonResponse.getString("message"))
                AppHelper.checkTokenResponse(code)
            },
            { error: Throwable ->
                Log.d(TAG, "loadItems error : " + error.localizedMessage)
            })
    }

    fun addItem(item: T, onResponse: (response: String?) -> Unit = {}) {
        service.create(item,
            { _: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "addItem response : " + jsonResponse.getString("message"))
                Log.d(TAG, "addItem response : " + jsonResponse.getString("data"))
                onResponse(jsonResponse.getString("data"))
            },
            { code: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "addItem response : " + jsonResponse.getString("message"))
                AppHelper.checkTokenResponse(code)
            },
            { error: Throwable ->
                Log.d(TAG, "addItem error : " + error.localizedMessage)
            })
    }

    fun modifyItem(item: T, onResponse: (response: String?) -> Unit = {}) {
        service.update(item.id, item,
            { _: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "modifyItem response : " + jsonResponse.getString("message"))
                onResponse(null)
            },
            { code: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "modifyItem response : " + jsonResponse.getString("message"))
                AppHelper.checkTokenResponse(code)
            },
            { error: Throwable ->
                Log.d(TAG, "modifyItem error : " + error.localizedMessage)
            })
    }

    fun modifyItemState(id: Int, state: Byte, onResponse: (response: String?) -> Unit = {}) {
        service.update(id, state,
            { _: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "modifyItemState response : " + jsonResponse.getString("message"))
                onResponse(null)
            },
            { code: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "modifyItemState response : " + jsonResponse.getString("message"))
                AppHelper.checkTokenResponse(code)
            },
            { error: Throwable ->
                Log.d(TAG, "modifyItemState error : " + error.localizedMessage)
            })
    }

    fun deleteItem(id: Int, onResponse: (response: String?) -> Unit = {}) {
        service.delete(id,
            { _: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "deleteItem response : " + jsonResponse.getString("message"))
                onResponse(null)
            },
            { code: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(TAG, "deleteItem response : " + jsonResponse.getString("message"))
                AppHelper.checkTokenResponse(code)
            },
            { error: Throwable ->
                Log.d(TAG, "deleteItem error : " + error.localizedMessage)
            })
    }
}