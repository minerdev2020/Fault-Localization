package com.minerdev.faultlocalization.viewmodel

import com.minerdev.faultlocalization.base.ItemViewModel
import com.minerdev.faultlocalization.model.Equipment
import com.minerdev.faultlocalization.model.Message
import com.minerdev.faultlocalization.repository.EquipmentRepository
import com.minerdev.faultlocalization.repository.MessageRepository
import com.minerdev.faultlocalization.utils.Constants.ID

open class EquipmentViewModel(
    private val messageRepository: MessageRepository,
    repository: EquipmentRepository
) :
    ItemViewModel<Equipment>(repository) {
    val messageStates = messageRepository.itemStates
    val messageTypes = messageRepository.itemTypes

    fun loadMessageStatesAndTypes() {
        messageRepository.loadItemStatesAndTypes()
    }

    fun addMessage(equipmentId: Int, typeId: Int, contents: String) {
        messageRepository.addItem(
            Message(
                type_id = typeId,
                state_id = 1,
                equipment_id = equipmentId,
                contents = contents,
                from_id = ID.toInt()
            )
        ) {}
    }
}