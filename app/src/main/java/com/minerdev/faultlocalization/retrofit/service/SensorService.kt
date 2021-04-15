package com.minerdev.faultlocalization.retrofit.service

import com.minerdev.faultlocalization.model.Sensor
import com.minerdev.faultlocalization.utils.Constants.API_SENSOR

object SensorService : ItemServiceImpl<Sensor>(API_SENSOR)