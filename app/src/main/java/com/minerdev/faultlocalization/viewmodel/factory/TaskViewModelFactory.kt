package com.minerdev.faultlocalization.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minerdev.faultlocalization.repository.TaskRepository
import com.minerdev.faultlocalization.viewmodel.TaskViewModel

class TaskViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            TaskViewModel(TaskRepository(context)) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}