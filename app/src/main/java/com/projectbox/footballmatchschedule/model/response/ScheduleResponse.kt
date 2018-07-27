package com.projectbox.footballmatchschedule.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by adinugroho
 */
data class ScheduleResponse(
        @SerializedName("events")
        val scheduleList: List<Schedule>
)

@Parcelize
data class Schedule(
        @SerializedName("idEvent")
        val scheduleID: String,

        @SerializedName("idHomeTeam")
        val homeID: String,

        @SerializedName("idAwayTeam")
        val awayID: String,

        @SerializedName("strHomeTeam")
        val homeTeam: String,

        @SerializedName("strAwayTeam")
        val awayTeam: String,

        @SerializedName("intHomeScore")
        val homeScore: Int?,

        @SerializedName("intAwayScore")
        val awayScore: Int?,

        @SerializedName("strHomeGoalDetails")
        val homeGoalDetails: String?,

        @SerializedName("strAwayGoalDetails")
        val awayGoalDetails: String?,

        @SerializedName("intHomeShots")
        val homeShots: Int?,

        @SerializedName("intAwayShots")
        val awayShots: Int?,

        @SerializedName("strHomeLineupGoalkeeper")
        val homeGK: String?,

        @SerializedName("strHomeLineupDefense")
        val homeDef: String?,

        @SerializedName("strHomeLineupMidfield")
        val homeMid: String?,

        @SerializedName("strHomeLineupForward")
        val homeFW: String?,

        @SerializedName("strAwayLineupGoalkeeper")
        val awayGK: String?,

        @SerializedName("strAwayLineupDefense")
        val awayDef: String?,

        @SerializedName("strAwayLineupMidfield")
        val awayMid: String?,

        @SerializedName("strAwayLineupForward")
        val awayFW: String?,

        @SerializedName("dateEvent")
        val date: String?,

        @SerializedName("strTime")
        val time: String?,

        @SerializedName("strSport")
        val sport: String
) : Parcelable