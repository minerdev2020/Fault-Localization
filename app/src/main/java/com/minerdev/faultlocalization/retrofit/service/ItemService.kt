package com.minerdev.faultlocalization.retrofit.service

import com.minerdev.faultlocalization.model.Item

interface ItemService {
    fun getStatesAndTypes(
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    )

    fun getAllItems(
        keyword: String = "",
        group1: Int = 0,
        group2: Int = 0,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    )

    fun getItem(
        id: Int,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    )

    fun createItem(
        item: Item,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    )

    fun updateItem(
        id: Int,
        item: Item,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    )

    fun updateItem(
        id: Int,
        state: Byte,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    )

    fun deleteItem(
        id: Int,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    )
}