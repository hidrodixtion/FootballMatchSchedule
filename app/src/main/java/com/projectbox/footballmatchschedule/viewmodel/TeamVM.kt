package com.projectbox.footballmatchschedule.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.projectbox.footballmatchschedule.model.League
import com.projectbox.footballmatchschedule.model.response.Team
import com.projectbox.footballmatchschedule.repository.TeamAPIRepository
import com.projectbox.footballmatchschedule.repository.TeamDBRepository
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * Created by adinugroho
 */
class TeamVM(private val api: TeamAPIRepository, private val db: TeamDBRepository) : ViewModel() {
    var teamList = MutableLiveData<List<Team>>()

    fun getTeams(league: League) {
        if (db.getTeamFromDB(league).isEmpty()) {
            launch(UI) {
                val list = api.getAllTeams(league.name)
                teamList.value = list
                db.insertTeamsToDB(list)
            }
        } else {
            teamList.value = db.getTeamFromDB(league)
        }
    }

    fun getFavoriteTeams() {
        teamList.value = db.getFavoriteTeams()
    }

    fun searchTeam(query: String) {
        launch(UI) {
            teamList.value = api.searchTeamFromAPI(query)
        }
    }
}