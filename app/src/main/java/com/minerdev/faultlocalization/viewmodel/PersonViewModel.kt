package com.minerdev.faultlocalization.viewmodel

import com.minerdev.faultlocalization.base.ItemViewModel
import com.minerdev.faultlocalization.model.Person
import com.minerdev.faultlocalization.repository.PersonRepository

open class PersonViewModel(repository: PersonRepository) : ItemViewModel<Person>(repository)