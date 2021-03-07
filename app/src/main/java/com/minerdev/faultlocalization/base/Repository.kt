package com.minerdev.faultlocalization.base

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.minerdev.faultlocalization.model.Item
import com.minerdev.faultlocalization.retrofit.ItemRetrofitManager
import com.minerdev.faultlocalization.utils.Constants.TAG
import org.json.JSONObject
import kotlin.reflect.KClass

open class Repository<T : Item>(
    private val context: Context,
    private val itemType: KClass<T>,
    private val onItemResponse: (response: String) -> T,
    private val onItemsResponse: (response: String) -> List<T>
) {
    val allItems = MutableLiveData<List<T>>()
    val item = MutableLiveData<T>()

    fun loadItem(id: Int) {
        ItemRetrofitManager.instance.getItem(itemType, id,
            { response: String ->
                run {
                    val data = JSONObject(response)
                    Log.d(TAG, "loadItem response : " + data.getString("message"))
                    Log.d(TAG, "loadItem response : " + data.getString("data"))

                    if (checkTokenResponse(data.getInt("code"))) {
                        item.postValue(onItemResponse(data.getString("data")))
                    }
                }
            },
            { error: Throwable ->
                run {
                    Log.d(TAG, "loadItem error : " + error.localizedMessage)
                }
            })
    }

    fun loadItems() {
        ItemRetrofitManager.instance.getAllItems(itemType,
            { response: String ->
                run {
                    val data = JSONObject(response)
                    Log.d(TAG, "loadItems response : " + data.getString("message"))
                    Log.d(TAG, "loadItems response : " + data.getString("data"))

                    if (checkTokenResponse(data.getInt("code"))) {
                        allItems.postValue(onItemsResponse(data.getString("data")))
                    }
                }
            },
            { error: Throwable ->
                run {
                    Log.d(TAG, "loadItems error : " + error.localizedMessage)
                }
            })
    }

    fun addItem(item: T) {
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

    fun modifyItem(item: T) {
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

    private fun checkTokenResponse(code: Int): Boolean {
        when (code) {
            200 -> {
                return true
            }
            401 -> {
                Toast.makeText(context, "无效的Token！", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            419 -> {
                Toast.makeText(context, "该Token已过期！请重新登录！", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            else -> {
                return false
            }
        }
    }
}