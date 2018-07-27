package com.projectbox.footballmatchschedule.model.response

import com.google.gson.annotations.SerializedName

/**
 * Created by adinugroho
 */
data class PlayerResponse(
        val player: List<Player>
)

data class Player(
        val idPlayer: String,

        @SerializedName("strPlayer")
        val name: String,

        @SerializedName("strPosition")
        val position: String,

        @SerializedName("strDescriptionEN")
        val description: String,

        @SerializedName("strCutout")
        val photo: String,

        @SerializedName("strFanart1")
        val fanart: String?
)