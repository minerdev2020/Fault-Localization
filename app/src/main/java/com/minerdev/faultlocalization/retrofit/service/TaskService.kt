package com.minerdev.faultlocalization.retrofit.service

import com.minerdev.faultlocalization.model.Task
import com.minerdev.faultlocalization.utils.Constants.API_TASK

object TaskService : ItemServiceImpl<Task>(API_TASK)