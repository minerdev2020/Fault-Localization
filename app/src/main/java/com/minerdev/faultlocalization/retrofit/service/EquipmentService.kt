package com.minerdev.faultlocalization.retrofit.service

import com.minerdev.faultlocalization.model.Equipment
import com.minerdev.faultlocalization.utils.Constants.API_EQUIPMENT

object EquipmentService : ItemServiceImpl<Equipment>(API_EQUIPMENT)