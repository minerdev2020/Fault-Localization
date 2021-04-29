package com.minerdev.faultlocalization.retrofit.service

import com.minerdev.faultlocalization.model.Alert
import com.minerdev.faultlocalization.utils.Constants.API_ALERT

object AlertService : ItemServiceImpl<Alert>(API_ALERT)