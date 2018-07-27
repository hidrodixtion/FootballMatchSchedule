package com.projectbox.footballmatchschedule.event

import com.projectbox.footballmatchschedule.model.response.Team

/**
 * Created by adinugroho
 */
data class TeamClickEvent(
        val team: Team
)