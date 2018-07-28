package com.projectbox.footballmatchschedule.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.database.sqlite.SQLiteConstraintException
import com.projectbox.footballmatchschedule.db.FavoriteScheduleColumns
import com.projectbox.footballmatchschedule.db.ManagedDB
import com.projectbox.footballmatchschedule.model.response.Schedule
import com.projectbox.footballmatchschedule.repository.ScheduleRepository
import com.projectbox.footballmatchschedule.repository.TeamRepository
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

/**
 * Created by adinugroho
 */
class ScheduleDetailVM(private val scheduleRepository: ScheduleRepository, private val teamRepository: TeamRepository) : ViewModel() {
    var isFavoriteSchedule = MutableLiveData<Boolean>()
    var message = MutableLiveData<String>()
    var homeBadge = MutableLiveData<String>()
    var awayBadge = MutableLiveData<String>()

    fun checkIfFavoriteSchedule(id: String) {
        isFavoriteSchedule.value = scheduleRepository.isFavoriteSchedule(id)
    }

    fun addToFavorite(schedule: Schedule) {
        val errMessage = scheduleRepository.addToFavorite(schedule)

        if (errMessage.isNullOrEmpty()) {
            isFavoriteSchedule.value = true
            message.value = "Schedule has been added to Favorites"
        } else {
            message.value = errMessage
        }
    }

    fun removeFromFavorite(schedule: Schedule) {
        val errMessage = scheduleRepository.removeFromFavorite(schedule.scheduleID)

        if (errMessage.isNullOrEmpty()) {
            isFavoriteSchedule.value = false
            message.value = "Schedule has been removed from Favorites"
        } else {
            message.value = errMessage
        }
    }

    fun getTeamBadges(homeID: String, awayID: String) {
        teamRepository.getTeamFromID(homeID) {
            homeBadge.value = it.teamBadge
        }

        teamRepository.getTeamFromID(awayID) {
            awayBadge.value = it.teamBadge
        }
    }
}