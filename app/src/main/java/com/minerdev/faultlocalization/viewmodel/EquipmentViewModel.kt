package com.minerdev.faultlocalization.viewmodel

import com.minerdev.faultlocalization.base.ItemViewModel
import com.minerdev.faultlocalization.model.Equipment
import com.minerdev.faultlocalization.repository.EquipmentRepository

open class EquipmentViewModel(repository: EquipmentRepository) :
    ItemViewModel<Equipment>(repository)