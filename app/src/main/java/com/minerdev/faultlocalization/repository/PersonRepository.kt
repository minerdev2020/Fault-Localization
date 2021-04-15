package com.minerdev.faultlocalization.repository

import com.minerdev.faultlocalization.base.ItemRepository
import com.minerdev.faultlocalization.model.Person
import com.minerdev.faultlocalization.retrofit.service.PersonService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class PersonRepository(service: PersonService) : ItemRepository<Person>(
    { response: String ->
        Json.decodeFromString(response)
    },
    { response: String ->
        Json.decodeFromString(response)
    },
    service
)