package com.projectbox.footballmatchschedule

import com.projectbox.footballmatchschedule.model.ScheduleResponse
import com.projectbox.footballmatchschedule.model.TeamResponse
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by adinugroho
 */
interface IService {
    @GET("eventspastleague.php?id=4328")
    fun getPastLeague(): Observable<ScheduleResponse>

    @GET("eventsnextleague.php?id=4328")
    fun getNextLeague(): Observable<ScheduleResponse>

    @GET("search_all_teams.php?l=English Premier League")
    fun getAllTeams(): Observable<TeamResponse>
}