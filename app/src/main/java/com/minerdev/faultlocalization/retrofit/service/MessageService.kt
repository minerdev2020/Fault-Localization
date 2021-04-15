package com.minerdev.faultlocalization.retrofit.service

import com.minerdev.faultlocalization.model.Message
import com.minerdev.faultlocalization.utils.Constants.API_MESSAGE

object MessageService : ItemServiceImpl<Message>(API_MESSAGE)