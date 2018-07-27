package com.projectbox.footballmatchschedule

import com.projectbox.footballmatchschedule.model.response.PlayerResponse
import com.projectbox.footballmatchschedule.model.response.ScheduleResponse
import com.projectbox.footballmatchschedule.model.response.TeamResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by adinugroho
 */
interface IService {
    @GET("eventspastleague.php")
    fun getPastLeague(@Query("id") leagueID: String = "4328"): Call<ScheduleResponse>//Observable<ScheduleResponse>

    @GET("eventsnextleague.php")
    fun getNextLeague(@Query("id") leagueID: String = "4328"): Call<ScheduleResponse>//Observable<ScheduleResponse>

    @GET("search_all_teams.php")
    fun getAllTeams(@Query("l") leagueName: String): Call<TeamResponse>//Observable<TeamResponse>

    @GET("lookup_all_players.php")
    fun getAllPlayers(@Query("id") teamID: String): Call<PlayerResponse>
}