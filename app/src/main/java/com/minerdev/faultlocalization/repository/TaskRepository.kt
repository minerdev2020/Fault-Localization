package com.minerdev.faultlocalization.repository

import com.minerdev.faultlocalization.base.ItemRepository
import com.minerdev.faultlocalization.model.Task
import com.minerdev.faultlocalization.retrofit.service.TaskService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class TaskRepository(service: TaskService) : ItemRepository<Task>(
    { response: String ->
        Json.decodeFromString(response)
    },
    { response: String ->
        Json.decodeFromString(response)
    },
    service
)