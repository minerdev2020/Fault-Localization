package com.minerdev.faultlocalization.viewmodel

import com.minerdev.faultlocalization.base.ItemViewModel
import com.minerdev.faultlocalization.model.Task
import com.minerdev.faultlocalization.repository.TaskRepository

class TaskViewModel(repository: TaskRepository) : ItemViewModel<Task>(repository)