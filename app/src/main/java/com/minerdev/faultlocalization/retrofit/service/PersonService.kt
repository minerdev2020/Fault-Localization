package com.minerdev.faultlocalization.retrofit.service

import com.minerdev.faultlocalization.model.Person
import com.minerdev.faultlocalization.utils.Constants.API_PERSON

object PersonService : ItemServiceImpl<Person>(API_PERSON)