package com.projectbox.footballmatchschedule.helper

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by adinugroho
 */
object DateConverter {
    fun convertFromScheduleDate(date: String?): String {
        val date = date ?: return "TBD"

        val sdfConvert = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val convertedDate = sdfConvert.parse(date)

        val sdfFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH)
        return sdfFormat.format(convertedDate)
    }
}