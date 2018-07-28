package com.projectbox.footballmatchschedule.helper

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by adinugroho
 */
object DateConverter {
    fun convertFromScheduleDate(date: String?): String {
        val d = date ?: return "TBD"

        val sdfConvert = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val convertedDate = sdfConvert.parse(d)

        val sdfFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH)
        return sdfFormat.format(convertedDate)
    }

    fun convertFromScheduleTime(time: String?): String {
        val t = time ?: return ""

        val sdfConvert: SimpleDateFormat
        val convertedTime: Date
        if (t.contains("+")) {
            sdfConvert = SimpleDateFormat("HH:mm:ssX", Locale.ENGLISH)
            convertedTime = sdfConvert.parse(t)
        } else {
            sdfConvert = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
            convertedTime = sdfConvert.parse(t)
        }

        val sdfFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdfFormat.format(convertedTime)
    }
}