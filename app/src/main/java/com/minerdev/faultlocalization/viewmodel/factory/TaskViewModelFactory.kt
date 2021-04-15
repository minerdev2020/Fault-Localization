package com.minerdev.faultlocalization.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minerdev.faultlocalization.repository.TaskRepository
import com.minerdev.faultlocalization.retrofit.service.TaskService
import com.minerdev.faultlocalization.viewmodel.TaskViewModel

class TaskViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            TaskViewModel(TaskRepository(TaskService)) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}