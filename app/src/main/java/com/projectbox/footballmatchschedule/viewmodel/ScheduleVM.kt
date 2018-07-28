package com.projectbox.footballmatchschedule.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.projectbox.footballmatchschedule.model.*
import com.projectbox.footballmatchschedule.model.response.Schedule
import com.projectbox.footballmatchschedule.repository.ScheduleAPIRepository
import com.projectbox.footballmatchschedule.repository.ScheduleDBRepository
import com.projectbox.footballmatchschedule.repository.TeamAPIRepository
import com.projectbox.footballmatchschedule.repository.TeamDBRepository
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import timber.log.Timber

/**
 * Created by adinugroho
 */
class ScheduleVM(private val api: ScheduleAPIRepository, private val db: ScheduleDBRepository, private val teamApi: TeamAPIRepository, private val teamDb: TeamDBRepository) : ViewModel() {
    var scheduleList = MutableLiveData<List<Schedule>>()

    fun getSchedule(scheduleType: ScheduleType, league: League? = null) {
        Timber.v("GET SCHEDULE")
        if (league != null) {
            if (teamDb.getTeamFromDB(league).isEmpty()) {
                launch(UI) {
                    val teams = teamApi.getAllTeams(league.name)
                    teamDb.insertTeamsToDB(teams)
                }
            }
        }

        when (scheduleType) {
            ScheduleType.Past, ScheduleType.Next -> {
                launch(UI) {
                    if (league != null) {
                        scheduleList.value = api.getSchedules(scheduleType, league.id)
                    }
                }
            }
            ScheduleType.Favorite -> {
                db.getFavoriteSchedules()?.let {
                    scheduleList.value = it
                }
            }
        }
    }

    fun searchSchedule(query: String) {
        launch(UI) {
            val result = api.searchSchedule(query)
            Timber.v("RESULT $result" )
            scheduleList.value = result
        }
    }
}