package com.projectbox.footballmatchschedule.model

import com.google.gson.annotations.SerializedName

/**
 * Created by adinugroho
 */
data class TeamResponse(
        val teams: List<Team>
)

data class Team(
        val idTeam: String,

        @SerializedName("strTeam")
        val teamName: String,

        @SerializedName("strTeamBadge")
        val teamBadge: String?,

        val idLeague: String,

        @SerializedName("strDescriptionEN")
        val teamDescription: String?,

        @SerializedName("intFormedYear")
        val formedYear: String?

)