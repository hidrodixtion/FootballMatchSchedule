package com.projectbox.footballmatchschedule

import com.projectbox.footballmatchschedule.model.ScheduleResponse
import com.projectbox.footballmatchschedule.model.TeamResponse
import io.reactivex.Observable
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by adinugroho
 */
interface IService {
    @GET("eventspastleague.php")
    fun getPastLeague(@Query("id") leagueID: String = "4328"): Call<ScheduleResponse>//Observable<ScheduleResponse>

    @GET("eventsnextleague.php")
    fun getNextLeague(@Query("id") leagueID: String = "4328"): Call<ScheduleResponse>//Observable<ScheduleResponse>

    @GET("search_all_teams.php?l=English Premier League")
    fun getAllTeams(): Call<TeamResponse>//Observable<TeamResponse>
}