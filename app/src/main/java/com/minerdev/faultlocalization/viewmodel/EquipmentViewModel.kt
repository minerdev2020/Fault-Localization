package com.minerdev.faultlocalization.viewmodel

import com.minerdev.faultlocalization.base.ItemViewModel
import com.minerdev.faultlocalization.model.Equipment
import com.minerdev.faultlocalization.model.Request
import com.minerdev.faultlocalization.repository.EquipmentRepository
import com.minerdev.faultlocalization.repository.RequestRepository
import com.minerdev.faultlocalization.utils.Constants.ID

class EquipmentViewModel(
    private val requestRepository: RequestRepository,
    repository: EquipmentRepository
) :
    ItemViewModel<Equipment>(repository) {
    val messageStates = requestRepository.itemStates
    val messageTypes = requestRepository.itemTypes

    fun loadMessageStatesAndTypes() {
        requestRepository.loadItemStatesAndTypes()
    }

    fun addMessage(equipmentId: Int, typeId: Int, estimatedTime: Float, contents: String) {
        requestRepository.addItem(
            Request(
                type_id = typeId,
                state_id = 1,
                equipment_id = equipmentId,
                estimated_time = estimatedTime,
                contents = contents,
                from_id = ID.toInt()
            )
        ) {}
    }
}