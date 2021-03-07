package com.minerdev.faultlocalization.viewmodel

import com.minerdev.faultlocalization.base.ItemViewModel
import com.minerdev.faultlocalization.model.Equipment
import com.minerdev.faultlocalization.model.Sensor
import com.minerdev.faultlocalization.repository.EquipmentRepository
import com.minerdev.faultlocalization.repository.SensorRepository

class EquipmentModifyViewModel(
    private val sensorRepository: SensorRepository,
    repository: EquipmentRepository
) :
    ItemViewModel<Equipment>(repository) {
    fun addSensor(sensor: Sensor) {
        sensorRepository.addItem(sensor)
    }

    fun modifySensor(sensor: Sensor) {
        sensorRepository.modifyItem(sensor)
    }

    fun modifySensorState(id: Int, state: Byte) {
        sensorRepository.modifyItemState(id, state)
    }

    fun deleteSensor(id: Int) {
        sensorRepository.deleteItem(id)
    }
}