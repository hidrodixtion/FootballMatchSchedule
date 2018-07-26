package com.projectbox.footballmatchschedule.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.database.sqlite.SQLiteConstraintException
import com.projectbox.footballmatchschedule.db.*
import com.projectbox.footballmatchschedule.model.*
import com.projectbox.footballmatchschedule.repository.ScheduleRepository
import com.projectbox.footballmatchschedule.repository.TeamRepository
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import timber.log.Timber

/**
 * Created by adinugroho
 */
class ScheduleVM(private val repository: ScheduleRepository, private val teamRepository: TeamRepository) : ViewModel() {
    var scheduleList = MutableLiveData<List<Schedule>>()

    fun getSchedule(scheduleType: ScheduleType, league: League) {
        Timber.v("GET SCHEDULE")
        if (teamRepository.getTeamFromDB(league).isEmpty()) {
            launch(UI) {
                val teams = teamRepository.getAllTeams(league.name)
                teamRepository.insertTeamsToDB(teams)
            }
        }

        when (scheduleType) {
            ScheduleType.Past, ScheduleType.Next -> {
                launch(UI) {
                    scheduleList.value = repository.getSchedules(scheduleType, league.id)
                }
            }
            ScheduleType.Favorite -> {
                repository.getFavoriteSchedules()?.let {
                    scheduleList.value = it
                }
            }
        }
    }
}