package com.minerdev.faultlocalization.repository

import com.minerdev.faultlocalization.base.Repository
import com.minerdev.faultlocalization.model.Person
import com.minerdev.faultlocalization.model.User
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class PersonRepository : Repository<Person>(
    Person::class,
    { response: String ->
        Json.decodeFromString(response)
    },
    { response: String ->
        Json.decodeFromString(response)
    })