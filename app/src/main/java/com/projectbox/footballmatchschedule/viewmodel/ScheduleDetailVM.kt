package com.projectbox.footballmatchschedule.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.projectbox.footballmatchschedule.model.response.Schedule
import com.projectbox.footballmatchschedule.model.response.Team
import com.projectbox.footballmatchschedule.repository.ScheduleAPIRepository
import com.projectbox.footballmatchschedule.repository.ScheduleDBRepository
import com.projectbox.footballmatchschedule.repository.TeamAPIRepository
import com.projectbox.footballmatchschedule.repository.TeamDBRepository
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * Created by adinugroho
 */
class ScheduleDetailVM(private val db: ScheduleDBRepository, private val teamAPI: TeamAPIRepository, private val teamDB: TeamDBRepository) : ViewModel() {
    var isFavoriteSchedule = MutableLiveData<Boolean>()
    var message = MutableLiveData<String>()
    var homeBadge = MutableLiveData<String>()
    var awayBadge = MutableLiveData<String>()

    fun checkIfFavoriteSchedule(id: String) {
        isFavoriteSchedule.value = db.isFavoriteSchedule(id)
    }

    fun addToFavorite(schedule: Schedule) {
        val errMessage = db.addToFavorite(schedule)

        if (errMessage.isNullOrEmpty()) {
            isFavoriteSchedule.value = true
            message.value = "Schedule has been added to Favorites"
        } else {
            message.value = errMessage
        }
    }

    fun removeFromFavorite(schedule: Schedule) {
        val errMessage = db.removeFromFavorite(schedule.scheduleID)

        if (errMessage.isNullOrEmpty()) {
            isFavoriteSchedule.value = false
            message.value = "Schedule has been removed from Favorites"
        } else {
            message.value = errMessage
        }
    }

    fun getTeamBadges(homeID: String, awayID: String) {
        getTeamFromID(homeID) {
            homeBadge.value = it.teamBadge
        }

        getTeamFromID(awayID) {
            awayBadge.value = it.teamBadge
        }
    }

    private fun getTeamFromID(id: String, callback: (Team) -> Unit) {
        val team = teamDB.getTeamFromDB(id)

        if (team == null) {
            launch(UI) {
                val listTeam = teamAPI.getTeamFromAPI(id)
                teamDB.insertTeamsToDB(listTeam)
                callback(listTeam.first())
            }
        } else {
            callback(team)
        }
    }
}