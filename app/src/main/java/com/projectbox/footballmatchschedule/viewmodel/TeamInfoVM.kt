package com.projectbox.footballmatchschedule.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.projectbox.footballmatchschedule.model.response.Team
import com.projectbox.footballmatchschedule.repository.TeamRepository

/**
 * Created by adinugroho
 */
class TeamInfoVM(private val repository: TeamRepository): ViewModel() {
    val team = MutableLiveData<Team>()
    val isFavoriteTeam = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()

    private lateinit var teamID: String

    fun getTeamFromID(teamID: String) {
        this.teamID = teamID

        val teamResult = repository.getTeamFromID(teamID)
        team.value = teamResult
        isFavoriteTeam.value = (teamResult.isFavorited == 1)
    }

    fun toggleFavorite() {
        val teamResult = repository.getTeamFromID(teamID)
        if (teamResult.isFavorited == 1) {
            isFavoriteTeam.value = repository.toggleFavorite(teamID, false)
            message.value = "Team has been removed from favorite"
        } else {
            isFavoriteTeam.value = repository.toggleFavorite(teamID, true)
            message.value = "Team has been added to favorite"
        }
    }
}