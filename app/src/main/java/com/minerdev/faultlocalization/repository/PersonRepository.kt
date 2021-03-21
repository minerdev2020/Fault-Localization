package com.minerdev.faultlocalization.repository

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import androidx.core.content.ContextCompat.startActivity
import com.minerdev.faultlocalization.base.Repository
import com.minerdev.faultlocalization.model.Person
import com.minerdev.faultlocalization.view.activity.SplashActivity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class PersonRepository(context: Context) : Repository<Person>(
    context,
    Person::class,
    { response: String ->
        Json.decodeFromString(response)
    },
    { response: String ->
        Json.decodeFromString(response)
    },
    {
        val intent = Intent(context, SplashActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP)
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(context, intent, null)
    })