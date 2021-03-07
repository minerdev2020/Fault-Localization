package com.minerdev.faultlocalization.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minerdev.faultlocalization.repository.MessageRepository
import com.minerdev.faultlocalization.viewmodel.MessageViewModel

class MessageViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
            MessageViewModel(MessageRepository(context)) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}