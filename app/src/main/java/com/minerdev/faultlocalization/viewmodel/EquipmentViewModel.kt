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
    fun addMessage(equipmentId: Int, contents: String) {
        messageRepository.addItem(
            Message(
                type_id = 1,
                state_id = 1,
                equipment_id = equipmentId,
                contents = contents,
                from_id = ID.toInt()
            )
        ) {}
    }
}