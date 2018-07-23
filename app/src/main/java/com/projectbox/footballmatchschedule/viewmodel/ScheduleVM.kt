package com.projectbox.footballmatchschedule.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.projectbox.footballmatchschedule.model.AppData
import com.projectbox.footballmatchschedule.model.Schedule
import com.projectbox.footballmatchschedule.model.ScheduleType
import com.projectbox.footballmatchschedule.repository.FavoriteManagedDB
import com.projectbox.footballmatchschedule.repository.FavoriteScheduleColumns
import com.projectbox.footballmatchschedule.repository.ScheduleRepository
import com.projectbox.footballmatchschedule.repository.TeamRepository
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

/**
 * Created by adinugroho
 */
class ScheduleVM(private val repository: ScheduleRepository, private val teamRepository: TeamRepository, private val db: FavoriteManagedDB) : ViewModel() {
    var scheduleList = MutableLiveData<List<Schedule>>()

    fun getSchedule(scheduleType: ScheduleType) {
        if (AppData.teamData.isEmpty()) {
            launch(UI) {
                AppData.teamData = teamRepository.getAllTeams()
            }
        }

        when (scheduleType) {
            ScheduleType.Past, ScheduleType.Next -> {
                launch(UI) {
                    scheduleList.value = repository.getSchedules(scheduleType)
                }
            }
            ScheduleType.Favorite -> getFavoriteSchedules()
        }
    }

    private fun getFavoriteSchedules() {
        db.use {
            val result = select(FavoriteScheduleColumns.TABLE_NAME)
            scheduleList.value = result.parseList(classParser())
        }
    }
}