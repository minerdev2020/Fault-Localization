package com.minerdev.faultlocalization.utils

import android.icu.text.SimpleDateFormat
import java.util.*

object Time {
    fun getDiffTimeMsg(since: String): String {
        val sinceTime = getTime(since)
        var diffTime = System.currentTimeMillis() / 1000 - sinceTime

        val msg: String
        when {
            diffTime < 60 -> msg = "방금전"
            60.let { diffTime /= it; diffTime } < 60 -> msg = diffTime.toString() + "분전"
            60.let { diffTime /= it; diffTime } < 24 -> msg = diffTime.toString() + "시간전"
            24.let { diffTime /= it; diffTime } < 30 -> msg = diffTime.toString() + "일전"
            30.let { diffTime /= it; diffTime } < 12 -> msg = diffTime.toString() + "개월전"
            else -> msg = diffTime.toString() + "년전"
        }

        return msg
    }

    fun getDate(time: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return format.parse(time.replace('T', ' ').substring(0, time.length - 5))
    }

    fun getHMS(time: String): String {
        val date = getDate(time)
        val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(date)
        return format.toString()
    }

    fun getTime(time: String): Long {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = format.parse(time.replace('T', ' ').substring(0, time.length - 5))
        return date.time
    }
}