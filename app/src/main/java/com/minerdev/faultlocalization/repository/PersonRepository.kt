package com.minerdev.faultlocalization.repository

import android.content.Context
import com.minerdev.faultlocalization.base.Repository
import com.minerdev.faultlocalization.model.Person
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class PersonRepository(context: Context) : Repository<Person>(
    context,
    Person::class,
    { response: String ->
        Json.decodeFromString(response)
    },
    { response: String ->
        Json.decodeFromString(response)
    })