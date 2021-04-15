package com.minerdev.faultlocalization.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minerdev.faultlocalization.repository.MessageRepository
import com.minerdev.faultlocalization.retrofit.service.MessageService
import com.minerdev.faultlocalization.viewmodel.MessageViewModel

class MessageViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
            MessageViewModel(MessageRepository(MessageService)) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}