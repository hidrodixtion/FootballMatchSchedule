package com.projectbox.footballmatchschedule.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.projectbox.footballmatchschedule.model.response.Team
import com.projectbox.footballmatchschedule.repository.TeamRepository

/**
 * Created by adinugroho
 */
class TeamInfoVM(val repository: TeamRepository): ViewModel() {
    val team = MutableLiveData<Team>()

    fun getTeamFromID(teamID: String) {
        team.value = repository.getTeamFromID(teamID)
    }
}