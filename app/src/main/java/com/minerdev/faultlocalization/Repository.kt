package com.minerdev.faultlocalization

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.minerdev.faultlocalization.model.Item
import com.minerdev.faultlocalization.retrofit.ItemRetrofitManager
import com.minerdev.faultlocalization.utils.Constants.TAG
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.json.JSONObject
import kotlin.reflect.KClass

class Repository<T : Item>(private val itemType: KClass<T>) {
    val allItems = MutableLiveData<List<T>>()
    val item = MutableLiveData<T>()

     @InternalSerializationApi
     fun loadItem(id: Int) {
        ItemRetrofitManager.instance.getItem(itemType, id,
            { response: String ->
                run {
                    val data = JSONObject(response)
                    Log.d(TAG, "loadItem response : " + data.getString("message"))
                    Log.d(TAG, "loadItem response : " + data.getString("data"))

                    val format = Json { encodeDefaults = true }
                    item.postValue(format.decodeFromString(itemType.serializer(), data.getString("data")))
                }
            },
            { error: Throwable ->
                run {
                    Log.d(TAG, "loadItem sale error : " + error.localizedMessage)
                }
            })
    }

    @InternalSerializationApi
    fun loadItems() {
        ItemRetrofitManager.instance.getAllItems(itemType,
            { response: String ->
                run {
                    val data = JSONObject(response)
                    Log.d(TAG, "loadItems sale response : " + data.getString("message"))
                    Log.d(TAG, "loadItems sale response : " + data.getString("data"))

                    val format = Json { encodeDefaults = true }
                    val items = format.decodeFromString(ListSerializer(itemType.serializer()), data.getString("data"))
                    allItems.postValue(items)
                }
            },
            { error: Throwable ->
                run {
                    Log.d(TAG, "loadItems sale error : " + error.localizedMessage)
                }
            })
    }

    fun addItem(item: T, onResponse: () -> Unit) {
        ItemRetrofitManager.instance.createItem(itemType, item,
            { response: String ->
                run {
                    val data = JSONObject(response)
                    Log.d(TAG, "addItem response : " + data.getString("message"))
                    Log.d(TAG, "addItem response : " + data.getString("data"))
                }
            },
            { error: Throwable ->
                run {
                    Log.d(TAG, "addItem error : " + error.localizedMessage)
                }
            })
    }

    fun modifyItem(item: T, onResponse: () -> Unit) {
        ItemRetrofitManager.instance.updateItem(itemType, item.id, item,
            { response: String ->
                run {
                    val data = JSONObject(response)
                    Log.d(TAG, "modifyItem response : " + data.getString("message"))
                }
            },
            { error: Throwable ->
                run {
                    Log.d(TAG, "modifyItem error : " + error.localizedMessage)
                }
            })
    }

    fun modifyItemState(id: Int, state: Byte) {
        ItemRetrofitManager.instance.updateItem(itemType, id, state,
            { response: String ->
                run {
                    val data = JSONObject(response)
                    Log.d(TAG, "modifyItemState response : " + data.getString("message"))
                }
            },
            { error: Throwable ->
                run {
                    Log.d(TAG, "modifyItemState error : " + error.localizedMessage)
                }
            })
    }

    fun deleteItem(id: Int) {
        ItemRetrofitManager.instance.deleteItem(itemType, id,
            { response: String ->
                run {
                    val data = JSONObject(response)
                    Log.d(TAG, "deleteItem response : " + data.getString("message"))
                }
            },
            { error: Throwable ->
                run {
                    Log.d(TAG, "deleteItem error : " + error.localizedMessage)
                }
            })
    }
}