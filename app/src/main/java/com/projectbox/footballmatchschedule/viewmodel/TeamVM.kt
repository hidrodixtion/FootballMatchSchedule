package com.projectbox.footballmatchschedule.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.projectbox.footballmatchschedule.db.ManagedDB
import com.projectbox.footballmatchschedule.model.League
import com.projectbox.footballmatchschedule.model.Team
import com.projectbox.footballmatchschedule.repository.TeamRepository
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * Created by adinugroho
 */
class TeamVM(private val teamRepository: TeamRepository) : ViewModel() {
    var teamList = MutableLiveData<List<Team>>()

    fun getTeams(league: League) {
        if (teamRepository.getTeamFromDB(league).isEmpty()) {
            launch(UI) {
                val list = teamRepository.getAllTeams(league.name)
                teamList.value = list
                teamRepository.insertTeamsToDB(list)
            }
        } else {
            teamList.value = teamRepository.getTeamFromDB(league)
        }
    }
}