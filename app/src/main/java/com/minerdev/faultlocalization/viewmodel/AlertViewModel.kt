package com.minerdev.faultlocalization.viewmodel

import com.minerdev.faultlocalization.base.ItemViewModel
import com.minerdev.faultlocalization.model.Alert
import com.minerdev.faultlocalization.repository.AlertRepository

class AlertViewModel(repository: AlertRepository) : ItemViewModel<Alert>(repository)