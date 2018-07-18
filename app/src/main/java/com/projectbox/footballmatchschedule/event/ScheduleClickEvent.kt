package com.projectbox.footballmatchschedule.event

import com.projectbox.footballmatchschedule.model.Schedule

/**
 * Created by adinugroho
 */
data class ScheduleClickEvent(
        val schedule: Schedule
)