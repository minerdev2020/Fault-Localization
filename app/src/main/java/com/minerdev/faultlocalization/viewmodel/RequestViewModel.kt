package com.minerdev.faultlocalization.viewmodel

import com.minerdev.faultlocalization.base.ItemViewModel
import com.minerdev.faultlocalization.model.Request
import com.minerdev.faultlocalization.repository.RequestRepository
import com.minerdev.faultlocalization.utils.Constants.ID

class RequestViewModel(repository: RequestRepository) : ItemViewModel<Request>(repository) {
    fun acceptRequest(request: Request) {
        request.reply_id = ID.toInt()
        request.state_id = 2
        modifyItem(request)
    }

    fun refuseRequest(request: Request) {
        request.reply_id = ID.toInt()
        request.state_id = 3
        modifyItem(request)
    }
}